#ifndef COMPONENT_MANAGER_HPP
#define COMPONENT_MANAGER_HPP

#include <unordered_map>
#include <memory>
#include <typeindex>
#include <typeinfo>
#include <stdexcept>
#include <iostream>
#include <string>

#include "ComponentArray.hpp"
#include "Core.hpp"

class ComponentManager
{
private:
	std::unordered_map<std::type_index, std::shared_ptr<IComponentArray>> componentsArray;

	ComponentManager() {};

	ComponentManager(const ComponentManager&) = delete;
	ComponentManager& operator=(const ComponentManager&) = delete;
public:
	static auto& getInstance()
	{
		static ComponentManager instance;
		return instance;
	}

	template<typename T>
	auto getComponentArray()
	{
		std::type_index typeName = std::type_index(typeid(T));

		auto it = componentsArray.find(typeName);
		if (it == componentsArray.end())
		{
			std::cerr << "Component not registered: " << typeName.name() << "\n";
			throw std::runtime_error("Component not registered.");
		}

		return std::static_pointer_cast<ComponentArray<T>>(it->second);
	}

	template<typename T>
	void registerComponent()
	{
		std::type_index typeName = std::type_index(typeid(T));

		if ((componentsArray.find(typeName)) != componentsArray.end())
		{
			std::cerr << "Component already registered: " << typeName.name() << "\n";
			throw std::runtime_error("Component already registered.");
		}

		componentsArray.insert({ typeName, std::make_shared<ComponentArray<T>>() });
	}

	template<typename T>
	void addComponent(Entity entity, T component)
	{
		getComponentArray<T>()->insertData(entity, component);
	}

	template<typename T>
	void removeComponent(Entity entity)
	{
		getComponentArray<T>()->removeData(entity);
	}

	template<typename T>
	T& getComponent(Entity entity)
	{
		const auto& componentArray = getComponentArray<T>();

		if (!componentArray->hasData(entity))
		{
			std::cerr << "Component not registered: " << std::type_index(typeid(T)).name() << "\n";
			throw std::runtime_error("Entity does not have component.");
		}

		return componentArray->getData(entity);
	}

	void entityDestroyed(Entity entity)
	{
		for (auto const& pair : componentsArray)
		{
			pair.second->destroyComponents(entity);
		}
	}

	// incredibly inefficient (but works for now)
	void allEntitiesDestroyed()
	{
		for (Entity i = 0; i < MAX_ENTITIES; ++i)
		{
			entityDestroyed(i);
		}
	}
};

#endif
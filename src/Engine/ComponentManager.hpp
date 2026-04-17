#ifndef COMPONENT_MANAGER_HPP
#define COMPONENT_MANAGER_HPP

#include "ComponentArray.hpp"

#include <unordered_map>
#include <memory>
#include <typeindex>
#include <typeinfo>
#include <stdexcept>

class ComponentManager
{
private:
	std::unordered_map<std::type_index, std::shared_ptr<IComponentArray>> componentsArray;

	ComponentManager() {};

	ComponentManager(const ComponentManager&) = delete;
	ComponentManager& operator=(const ComponentManager&) = delete;
public:
	template<typename T>
	void registerComponent()
	{
		std::type_index typeName = std::type_index(typeid(T));

		if ((componentsArray.find(typeName)) != componentsArray.end())
		{
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
		return getComponentArray<T>()->getData(entity);
	}

	void entityDestroyed(Entity entity)
	{
		for (auto const& pair : componentsArray)
		{
			pair.second->destroyComponents(entity);
		}
	}

	template<typename T>
	std::shared_ptr<ComponentArray<T>> getComponentArray()
	{
		std::type_index typeName = std::type_index(typeid(T));

		auto it = componentsArray.find(typeName);
		if (it == componentsArray.end())
		{
			throw std::runtime_error("Component not registered.");
		}

		return std::static_pointer_cast<ComponentArray<T>>(it->second);
	}

	static ComponentManager& getInstance()
	{
		static ComponentManager instance;
		return instance;
	}
};

#endif
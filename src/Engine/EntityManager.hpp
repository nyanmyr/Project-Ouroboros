#ifndef ENTITY_MANAGER_HPP
#define ENTITY_MANAGER_HPP

#include <vector>
#include <queue>
#include <stdexcept>

#include "Core.hpp"

class EntityManager
{
private:
	std::queue<Entity> availableEntities;
	std::vector<bool> alive;
	Entity livingCount = 0;

	EntityManager()
	{
		alive.resize(MAX_ENTITIES, false);

		for (Entity entity = 0; entity < MAX_ENTITIES; ++entity)
		{
			availableEntities.push(entity);
		}
	}

	EntityManager(const EntityManager&) = delete;
	EntityManager& operator=(const EntityManager&) = delete;
public:
	static EntityManager& getInstance()
	{
		static EntityManager instance;
		return instance;
	}

	Entity createEntity()
	{
		if (livingCount >= MAX_ENTITIES)
		{
			throw std::runtime_error("Too many entities");
		}

		Entity id = availableEntities.front();
		availableEntities.pop();
		alive[id] = true;
		livingCount++;
		return id;
	}

	void destroyEntity(Entity entity)
	{
		if (entity >= alive.size() || !alive[entity] )
		{
			throw std::runtime_error("Too many entities");
		}

		alive[entity] = false;
		availableEntities.push(entity);
		--livingCount;
	}

	bool isAlive(Entity entity) const
	{
		return alive[entity];
	}
};

#endif
#include "Headers/Systems.hpp"

#include <SFML/Graphics.hpp>

void RenderSystem(sf::RenderWindow& window)
{
    ComponentManager& cm = ComponentManager::getInstance();

    auto& shapes = cm.getComponentArray<CShape>();
    auto& positions = cm.getComponentArray<CPosition>();

    for (auto& [entity, shape] : shapes->getAll())
    {
        if (positions->hasData(entity))
        {
            CPosition pos = positions->getData(entity);
            shape.rect.setPosition(pos.x, pos.y);
            window.draw(shape.rect);
        }
    }
}

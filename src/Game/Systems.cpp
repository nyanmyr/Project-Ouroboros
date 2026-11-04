#include "Headers/Systems.hpp"

#include <SFML/Graphics.hpp>

#include <algorithm>
#include <utility>
#include <vector>
#include <stdexcept>

#include "../Engine/NacreCoordinator.hpp"
#include "Headers/Components.hpp"
#include "Headers/Enums.hpp"
#include "Headers/Events.hpp"

// -------------------------------------------------------
// start systems
// -------------------------------------------------------
namespace Start
{
	void setText()
	{
		const auto& textArray = nc().getComponentArray<Component::Text>();

		for (auto& [entity, text] : textArray->getAll())
		{
			nc().call<Event::SetTextString>
				(
					Event::SetTextString
					{ entity, text.string, text.box }
				);
			nc().call<Event::SetTextSize>
				(
					Event::SetTextSize
					{ entity, text.size, text.box }
				);
			nc().call<Event::SetTextColor>
				(
					Event::SetTextColor
					{ entity, text.color, text.box }
				);
		}
	}
	void setTextOrigin()
	{
		const auto& textArray = nc().getComponentArray<Component::Text>();

		for (auto& [entity, text] : textArray->getAll())
		{
			nc().call<Event::SetTextOrigin>
				(
					Event::SetTextOrigin
					{ entity, text }
				);
		}
	}
	void setSpriteOrigin()
	{
		const auto& originOffsetsArray = nc().getComponentArray<Component::OriginOffsets>();
		const auto& originPointArray = nc().getComponentArray<Component::OriginPoint>();

		for (auto& [entity, origin] : originOffsetsArray->getAll())
		{
			if (!originPointArray->hasData(entity)) continue;

			auto& originPoint = originPointArray->getData(entity);

			nc().call<Event::SetSpriteOrigin>
				(
					Event::SetSpriteOrigin
					{ entity, origin, originPoint }
				);
		}
	}
	void loadTextures(Component::Singleton::TexturesContainer& loadedTextures)
	{
		loadedTextures.map.emplace(Enum::Texture::TEXTURE_PLACEHOLDER, sf::Texture(SPRITES_PATH "placeholder_texture.jpeg"));
	}
	void loadSprites(const Component::Singleton::TexturesContainer& loadedTextures)
	{
		const auto& spriteArray = nc().getComponentArray<Component::Sprite>();

		for (auto& [entity, sprite] : spriteArray->getAll())
		{
			nc().call<Event::LoadSprite>
				(
					Event::LoadSprite
					{ entity, sprite, loadedTextures }
				);
		}
	}
	void setColor()
	{
		const auto& colorArray = nc().getComponentArray<Component::Color>();

		for (auto& [entity, color] : colorArray->getAll())
		{
			nc().call<Event::SetColor>
				(
					Event::SetColor
					{ entity, color }
				);
		}
	}
}

// -------------------------------------------------------
// control systems
// -------------------------------------------------------
namespace Control
{
	void doPlayerControl
	(
		const Entity player,
		const DeltaTime dt
	)
	{
		auto& velocity = nc().getComponent<Component::Velocity>(player);
		const auto& playerController = nc().getComponent<Component::PlayerController>(player);
		const auto& acceleration = nc().getComponent<Component::Acceleration>(player);

		if (!playerController.enabled)
		{
			return;
		}

		double newAccelerationX = 0.0;
		double newAccelerationY = 0.0;

		if (sf::Keyboard::isKeyPressed(sf::Keyboard::Key::W))
		{
			newAccelerationY = -acceleration.y;
		}
		else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Key::S))
		{
			newAccelerationY = acceleration.y;
		}

		if (sf::Keyboard::isKeyPressed(sf::Keyboard::Key::A))
		{
			newAccelerationX = -acceleration.x;
		}
		else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Key::D))
		{
			newAccelerationX = acceleration.x;
		}

		// applies the acceleration (even if there aren't any changes)
		velocity.x += (newAccelerationX * dt);
		velocity.y += (newAccelerationY * dt);

		if (velocity.x > velocity.maxX)
		{
			velocity.x = velocity.maxX;
		}
		else if (velocity.x < velocity.minX)
		{
			velocity.x = velocity.minX;
		}

		if (velocity.y > velocity.maxY)
		{
			velocity.y = velocity.maxY;
		}
		else if (velocity.y < velocity.minY)
		{
			velocity.y = velocity.minY;
		}

	}
}

// -------------------------------------------------------
// update systems
// -------------------------------------------------------
namespace Update
{
	void doButtonTimer(const DeltaTime dt)
	{
		const auto& buttonArray = nc().getComponentArray<Component::Button>();

		for (auto& [entity, button] : buttonArray->getAll())
		{
			if (button.timer <= 0.0)
			{
				continue;
			}

			button.timer -= dt;

			if (button.timer <= 0.0) button.timer = 0.0;
		}
	}

	template<typename T>
	static T easeOutElastic(T current, T max, T min)
	{
		T progress = 1.0 - [](T target, T min, T max)
			{
				return (target - min) / (max - min);
			}
		(current, min, max);

		static const double c4 = (2 * std::acos(-1.0)) / 3;

		if (progress <= 0) return 0.0;
		else if (progress >= 1) return 1.0;

		return std::pow(2.0, -10.0 * progress) *
			std::sin((progress * 10.0 - 0.75) * c4) + 1.0;
	}

	static constexpr double DEFAULT_SCALE_X = 1.0;
	static constexpr double DEFAULT_SCALE_Y = 1.0;

	static constexpr double HOVER_SCALE_X = 1.1;
	static constexpr double HOVER_SCALE_Y = 1.1;

	static constexpr double CLICKED_SCALE_X = 0.9;
	static constexpr double CLICKED_SCALE_Y = 0.9;

	void applyButtonHoverAnimation()
	{
		const auto& buttonArray = nc().getComponentArray<Component::Button>();

		for (auto& [entity, button] : buttonArray->getAll())
		{
			if (button.timer <= 0.0 ||
				button.state != Enum::ButtonState::HOVERED)
			{
				continue;
			}

			auto& transform = nc().getComponent<Component::Transform>(entity);
			auto& sprite = nc().getComponent<Component::Sprite>(entity);

			double scalingFactor = easeOutElastic
			(
				button.timer,
				button.hoverAnimationDuration,
				0.0
			);

			sprite.body->setScale
			(
				sf::Vector2f
				(
					(HOVER_SCALE_X * (transform.width / sprite.body->getTexture().getSize().x)) * scalingFactor,
					(HOVER_SCALE_Y * (transform.height / sprite.body->getTexture().getSize().y)) * scalingFactor
				)
			);

			auto& text = nc().getComponent<Component::Text>(entity);
			text.box->setScale
			(
				sf::Vector2f
				(
					HOVER_SCALE_X * scalingFactor,
					HOVER_SCALE_Y * scalingFactor
				)
			);
		}
	}
	void applyButtonIdleAnimation()
	{
		const auto& buttonArray = nc().getComponentArray<Component::Button>();

		for (auto& [entity, button] : buttonArray->getAll())
		{
			if (button.timer <= 0.0 ||
				button.state != Enum::ButtonState::IDLE)
			{
				continue;
			}

			auto& transform = nc().getComponent<Component::Transform>(entity);
			auto& sprite = nc().getComponent<Component::Sprite>(entity);

			double scalingFactor = easeOutElastic
			(
				button.timer,
				button.idleAnimationDuration,
				0.0
			);

			sprite.body->setScale
			(
				sf::Vector2f
				(
					(DEFAULT_SCALE_X * (transform.width / sprite.body->getTexture().getSize().x)) * scalingFactor,
					(DEFAULT_SCALE_X * (transform.height / sprite.body->getTexture().getSize().y)) * scalingFactor
				)
			);

			auto& text = nc().getComponent<Component::Text>(entity);
			text.box->setScale
			(
				sf::Vector2f
				(
					DEFAULT_SCALE_X * scalingFactor,
					DEFAULT_SCALE_X * scalingFactor
				)
			);
		}
	}
	void applyButtonClickedAnimation()
	{
		const auto& buttonArray = nc().getComponentArray<Component::Button>();

		for (auto& [entity, button] : buttonArray->getAll())
		{
			if (button.timer <= 0.0 ||
				button.state != Enum::ButtonState::CLICKED)
			{
				continue;
			}

			auto& transform = nc().getComponent<Component::Transform>(entity);
			auto& sprite = nc().getComponent<Component::Sprite>(entity);

			double scalingFactor = easeOutElastic
			(
				button.timer,
				button.clickedAnimationDuration,
				0.0
			);

			sprite.body->setScale
			(
				sf::Vector2f
				(
					(CLICKED_SCALE_X * (transform.width / sprite.body->getTexture().getSize().x)) * scalingFactor,
					(CLICKED_SCALE_X * (transform.height / sprite.body->getTexture().getSize().y)) * scalingFactor
				)
			);

			auto& text = nc().getComponent<Component::Text>(entity);
			text.box->setScale
			(
				sf::Vector2f
				(
					CLICKED_SCALE_X * scalingFactor,
					CLICKED_SCALE_X * scalingFactor
				)
			);
		}
	}
	void enableButtonNextScene()
	{
		const auto& buttonArray = nc().getComponentArray<Component::Button>();

		for (auto& [entity, button] : buttonArray->getAll())
		{
			if (button.timer > 0.0 ||
				button.state != Enum::ButtonState::CLICKED)
			{
				continue;
			}

			const auto& nextScene = nc().getComponent<Component::NextScene>(entity);

			nc().call<Event::PlayScene>(Event::PlayScene{ nextScene.next });

			break;
		}
	}

	static constexpr double BOUNCE_FACTOR = 0.5;

	void boundaryBounce
	(
		const double left,
		const double right,
		const double top,
		const double bottom
	)
	{
		const auto& velocityArray = nc().getComponentArray<Component::Velocity>();

		for (auto& [entity, velocity] : velocityArray->getAll())
		{
			auto& pos = nc().getComponent<Component::Position>(entity);
			auto& trans = nc().getComponent<Component::Transform>(entity);
			auto& originOffsets = nc().getComponent<Component::OriginOffsets>(entity);

			if (pos.x + (trans.width - originOffsets.x) > right)
			{
				pos.x = right - (trans.width - originOffsets.x);
				velocity.x = -velocity.x * BOUNCE_FACTOR;
			}
			else if (pos.x - (trans.width - originOffsets.x) < left)
			{
				pos.x = left + (trans.width - originOffsets.x);
				velocity.x = -velocity.x * BOUNCE_FACTOR;
			}

			if (pos.y + (trans.height - originOffsets.y) > bottom)
			{
				pos.y = bottom - (trans.height - originOffsets.y);
				velocity.y = -velocity.y * BOUNCE_FACTOR;
			}
			else if (pos.y - (trans.height - originOffsets.y) < top)
			{
				pos.y = top + (trans.height - originOffsets.y);
				velocity.y = -velocity.y * BOUNCE_FACTOR;
			}
		}
	}
	void move(const DeltaTime dt)
	{
		const auto& velocityArray = nc().getComponentArray<Component::Velocity>();

		for (auto& [entity, velocity] : velocityArray->getAll())
		{
			auto& pos = nc().getComponent<Component::Position>(entity);
			pos.x += (velocity.x * dt);
			pos.y += (velocity.y * dt);
		}
	}
	void drag(const DeltaTime dt)
	{
		const auto& velocityArray = nc().getComponentArray<Component::Velocity>();

		for (auto& [entity, velocity] : velocityArray->getAll())
		{
			const auto& drag = nc().getComponent<Component::Drag>(entity);

			// can't be exactly 0.0 because it will drift aimlessly
			velocity.x = velocity.x < -0.1 ? velocity.x + (drag.x * dt) :
				velocity.x > 0.1f ? velocity.x - (drag.x * dt) : 0.0;
			velocity.y = velocity.y < -0.1 ? velocity.y + (drag.y * dt) :
				velocity.y > 0.1f ? velocity.y - (drag.y * dt) : 0.0;
		}
	}
}

// -------------------------------------------------------
// rendering systems
// -------------------------------------------------------
namespace Render
{
	void doZIndex(std::queue<Entity>& renderQueue)
	{
		const auto& zIndexArray = nc().getComponentArray<Component::ZIndex>();

		std::vector<std::pair<int, Entity>> renderVector;
		for (auto& [entity, zIndex] : nc().getComponentArray<Component::ZIndex>()->getAll())
		{
			if (zIndex.visible) renderVector.emplace_back(zIndex.index, entity);
		}
		std::sort(renderVector.begin(), renderVector.end());

		for (auto& [zIndex, entity] : renderVector)
		{
			renderQueue.push(entity);
		}
	}
	void render
	(
		sf::RenderWindow& window,
		std::queue<Entity>& renderQueue
	)
	{
		const auto& spriteArray = nc().getComponentArray<Component::Sprite>();
		const auto& positionArray = nc().getComponentArray<Component::Position>();
		const auto& textArray = nc().getComponentArray<Component::Text>();

		while (!renderQueue.empty())
		{
			Entity& popped = renderQueue.front();
			////std::cout << "popped: " << popped << "\n";

			if (!positionArray->hasData(popped))
			{
				// this means the entity does not have a position component
				continue;
			}

			auto& pos = positionArray->getData(popped);

			if (spriteArray->hasData(popped))
			{
				auto& sprite = spriteArray->getData(popped);

				sprite.body->setPosition
				(
					sf::Vector2f
					(
						pos.x,
						pos.y
					)
				);
				window.draw(sprite.body.value());
			}

			if (textArray->hasData(popped))
			{
				auto& text = textArray->getData(popped);

				text.box->setPosition
				(
					sf::Vector2f
					(
						pos.x,
						pos.y
					)
				);
				window.draw(text.box.value());
			}

			renderQueue.pop();
		}
	}
}
#include "Headers/Listeners.hpp"
#include "Headers/Components.hpp"
#include "Headers/Scenes.hpp"

#include <stdexcept>

static bool checkCollision
(
	sf::Vector2f a,
	sf::Vector2f b,
	sf::Vector2f aTrans,
	sf::Vector2f bTrans
)
{
	// AABB collision
	if (a.x + (aTrans.x / 2.f) > b.x - (bTrans.x / 2.f) &&
		a.x - (aTrans.x / 2.f) < b.x + (bTrans.x / 2.f) &&
		a.y + (aTrans.y / 2.f) > b.y - (bTrans.y / 2.f) &&
		a.y - (aTrans.y / 2.f) < b.y + (bTrans.y / 2.f))
	{
		return true;
	}

	return false;
}

namespace Listener
{
	void setSpriteOrigin(Event::SetSpriteOrigin event)
	{
		if (event.target == NULL_ENTITY) throw std::runtime_error("Error: null entity");

		auto& sprite = nc().getComponent<Component::Sprite>(event.target);
		const auto& transform = nc().getComponent<Component::Transform>(event.target);

		switch (event.originPoint.data)
		{
		case Enum::OriginPoint::LEFT:
			event.originOffsets.x = 0.0;
			event.originOffsets.y = transform.height / 2.0;
			break;
		case Enum::OriginPoint::RIGHT:
			event.originOffsets.x = transform.width;
			event.originOffsets.y = transform.height / 2.0;
			break;
		case Enum::OriginPoint::TOP:
			event.originOffsets.x = transform.width / 2.0;
			event.originOffsets.y = 0.0;
			break;
		case Enum::OriginPoint::BOTTOM:
			event.originOffsets.x = transform.width / 2.0;
			event.originOffsets.y = transform.height;
			break;
		case Enum::OriginPoint::TOP_LEFT:
			event.originOffsets.x = 0.0;
			event.originOffsets.y = 0.0;
			break;
		case Enum::OriginPoint::TOP_RIGHT:
			event.originOffsets.x = transform.width;
			event.originOffsets.y = 0.0;
			break;
		case Enum::OriginPoint::BOTTOM_LEFT:
			event.originOffsets.x = 0.0;
			event.originOffsets.y = transform.height;
			break;
		case Enum::OriginPoint::BOTTOM_RIGHT:
			event.originOffsets.x = transform.width;
			event.originOffsets.y = transform.height;
			break;
		case Enum::OriginPoint::CENTER:
		default:
			event.originOffsets.x = transform.width / 2.0;
			event.originOffsets.y = transform.height / 2.0;
			break;
		}

		//std::cout << "x: " << (event.originOffsets.x / sprite.body->getScale().x) << "\n";
		//std::cout << "y: " << (event.originOffsets.y / sprite.body->getScale().y) << "\n";

		sprite.body->setOrigin
		(
			sf::Vector2f
			(
				event.originOffsets.x / sprite.body->getScale().x,
				event.originOffsets.y / sprite.body->getScale().y
			)
		);
	}
	void setColor(Event::SetColor event)
	{
		if (event.target == NULL_ENTITY) throw std::runtime_error("Error: null entity");

		auto& sprite = nc().getComponent<Component::Sprite>(event.target);

		sprite.body->setColor(event.color.data);
	}
	void loadSprite(Event::LoadSprite event)
	{
		if (event.target == NULL_ENTITY) throw std::runtime_error("Error: null entity");

		auto& texture = nc().getComponent<Component::Texture>(event.target);
		auto& transform = nc().getComponent<Component::Transform>(event.target);

		if (event.loadedTextures.map.find(texture.data) == event.loadedTextures.map.end())
		{
			throw std::runtime_error("Texture has not been loaded.");
		}

		event.sprite.body.emplace(event.loadedTextures.map.at(texture.data));
		event.sprite.body->setScale
		(
			sf::Vector2f
			(
				transform.width / event.sprite.body->getGlobalBounds().size.x,
				transform.height / event.sprite.body->getGlobalBounds().size.y
			)
		);
	}

	void setTextString(Event::SetTextString event)
	{
		if (event.target == NULL_ENTITY) throw std::runtime_error("Error: null entity");

		event.text->setString(event.string);
	}
	void setTextSize(Event::SetTextSize event)
	{
		if (event.target == NULL_ENTITY) throw std::runtime_error("Error: null entity");

		event.text->setCharacterSize(event.size);
	}
	void setTextColor(Event::SetTextColor event)
	{
		if (event.target == NULL_ENTITY) throw std::runtime_error("Error: null entity");

		event.text->setFillColor(event.color);
	}
	void setTextOrigin(Event::SetTextOrigin event)
	{
		if (event.target == NULL_ENTITY) throw std::runtime_error("Error: null entity");

		const auto bounds = event.text.box.value().getLocalBounds();

		const float left = bounds.position.x;
		const float right = left + bounds.size.x;
		const float top = bounds.position.y;
		const float bottom = top + bounds.size.y;

		const float centerX = left + bounds.size.x / 2.f;
		const float centerY = top + bounds.size.y / 2.f;

		float offsetX;
		float offsetY;

		switch (event.text.format)
		{
		case Enum::TextFormat::LEFT:
			offsetX = left;
			offsetY = centerY;
			break;
		case Enum::TextFormat::RIGHT:
			offsetX = right;
			offsetY = centerY;
			break;
		case Enum::TextFormat::TOP:
			offsetX = centerX;
			offsetY = top;
			break;
		case Enum::TextFormat::BOTTOM:
			offsetX = centerX;
			offsetY = bottom;
			break;
		case Enum::TextFormat::TOP_LEFT:
			offsetX = left;
			offsetY = top;
			break;
		case Enum::TextFormat::TOP_RIGHT:
			offsetX = right;
			offsetY = top;
			break;
		case Enum::TextFormat::BOTTOM_LEFT:
			offsetX = left;
			offsetY = bottom;
			break;
		case Enum::TextFormat::BOTTOM_RIGHT:
			offsetX = right;
			offsetY = bottom;
			break;
		case Enum::TextFormat::CENTER:
		default:
			offsetX = bounds.position.x + bounds.size.x / 2.f;
			offsetY = bounds.position.y + bounds.size.y / 2.f;
			break;
		}

		event.text.box->setOrigin
		(
			sf::Vector2f
			(
				offsetX,
				offsetY
			)
		);
	}

	void clickedButton(Event::MouseClick event)
	{
		const auto& buttonArray = nc().getComponentArray<Component::Button>();

		for (auto& [entity, button] : buttonArray->getAll())
		{
			if (button.state == Enum::ButtonState::CLICKED ||
				!button.enabled)
			{
				continue;
			}

			const auto& originOffset = nc().getComponent<Component::OriginOffsets>(entity);
			const auto& transform = nc().getComponent<Component::Transform>(entity);
			const auto& pos = nc().getComponent<Component::Position>(entity);

			if (checkCollision
			(
				event.pos,
				sf::Vector2f
				{
					static_cast<float>(pos.x - originOffset.x + (transform.width / 2.f)),
					static_cast<float>(pos.y - originOffset.y + (transform.height / 2.f))
				},
				sf::Vector2f
				{
					0.f,
					0.f
				},
				sf::Vector2f
				{
					static_cast<float>(transform.width),
					static_cast<float>(transform.height)
				}
			)
				)
			{
				button.timer = button.clickedAnimationDuration;
				button.state = Enum::ButtonState::CLICKED;
			}
		}
	}
	void hoverButton(Event::MouseMoved event)
	{
		const auto& buttonArray = nc().getComponentArray<Component::Button>();

		for (auto& [entity, button] : buttonArray->getAll())
		{
			if (button.timer > 0.0 ||
				button.state == Enum::ButtonState::HOVERED ||
				button.state == Enum::ButtonState::CLICKED ||
				!button.enabled)
			{
				continue;
			}

			const auto& originOffset = nc().getComponent<Component::OriginOffsets>(entity);
			const auto& transform = nc().getComponent<Component::Transform>(entity);
			const auto& pos = nc().getComponent<Component::Position>(entity);

			if (checkCollision
			(
				event.pos,
				sf::Vector2f
				{
					static_cast<float>(pos.x - originOffset.x + (transform.width / 2.f)),
					static_cast<float>(pos.y - originOffset.y + (transform.height / 2.f))
				},
				sf::Vector2f
				{
					0.f,
					0.f
				},
				sf::Vector2f
				{
					static_cast<float>(transform.width),
					static_cast<float>(transform.height)
				}
			)
				)
			{
				button.timer = button.hoverAnimationDuration;
				button.state = Enum::ButtonState::HOVERED;
			}
		}
	}
	void idleButton(Event::MouseMoved event)
	{
		const auto& buttonArray = nc().getComponentArray<Component::Button>();

		for (auto& [entity, button] : buttonArray->getAll())
		{
			if (button.timer > 0.0 ||
				button.state == Enum::ButtonState::IDLE ||
				button.state == Enum::ButtonState::CLICKED ||
				!button.enabled)
			{
				continue;
			}

			const auto& originOffset = nc().getComponent<Component::OriginOffsets>(entity);
			const auto& transform = nc().getComponent<Component::Transform>(entity);
			const auto& pos = nc().getComponent<Component::Position>(entity);

			if (checkCollision
			(
				event.pos,
				sf::Vector2f
				{
					static_cast<float>(pos.x - originOffset.x + (transform.width / 2.f)),
					static_cast<float>(pos.y - originOffset.y + (transform.height / 2.f))
				},
				sf::Vector2f
				{
					0.f,
					0.f
				},
				sf::Vector2f
				{
					static_cast<float>(transform.width),
					static_cast<float>(transform.height)
				}
			)
				)
			{
				continue;
			}

			button.timer = button.idleAnimationDuration;
			button.state = Enum::ButtonState::IDLE;
		}
	}

	void playNextScene(Event::PlayScene event)
	{
		nc().destroyAll();
		playScene(event.next);
	}
}
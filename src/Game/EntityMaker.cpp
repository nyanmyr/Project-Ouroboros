#include "Headers/EntityMaker.hpp"

#include <SFML/Graphics.hpp>
#include "../Engine/NacreCoordinator.hpp"
#include "Headers/Enums.hpp"

namespace EntityMaker
{
	Entity player
	(
		const Component::Drag& drag,
		const Component::Color& color,
		const Component::ZIndex& zIndex,
		const Component::Texture& texture,
		const Component::Position& position,
		const Component::Velocity& velocity,
		const Component::Transform& transform,
		const Component::OriginPoint& originPoint,
		const Component::Acceleration& acceleration
	)
	{
		Entity entity = nc().createEntity();

		nc().addComponent
		(
			entity,
			drag
		);

		nc().addComponent
		(
			entity,
			color
		);

		nc().addComponent
		(
			entity,
			zIndex
		);

		nc().addComponent
		(
			entity,
			Component::Sprite{}
		);

		nc().addComponent
		(
			entity,
			texture
		);

		nc().addComponent
		(
			entity,
			position
		);
		
		nc().addComponent
		(
			entity,
			velocity
		);

		nc().addComponent
		(
			entity,
			transform
		);

		nc().addComponent
		(
			entity,
			Component::OriginOffsets{}
		);

		nc().addComponent
		(
			entity,
			originPoint
		);

		nc().addComponent
		(
			entity,
			acceleration
		);
		
		nc().addComponent
		(
			entity,
			Component::PlayerController{}
		);

		return entity;
	}

	Entity button
	(
		const Component::Text& text,
		const Component::Color& color,
		const Component::Button& button,
		const Component::ZIndex& zIndex,
		const Component::Texture& texture,
		const Component::Position& position,
		const Component::NextScene& nextScene,
		const Component::Transform& transform,
		const Component::OriginPoint& originPoint
	)
	{
		Entity entity = nc().createEntity();

		nc().addComponent
		(
			entity,
			text
		);

		nc().addComponent
		(
			entity,
			color
		);

		nc().addComponent
		(
			entity,
			button
		);

		nc().addComponent
		(
			entity,
			zIndex
		);

		nc().addComponent
		(
			entity,
			Component::Sprite{}
		);

		nc().addComponent
		(
			entity,
			texture
		);

		nc().addComponent(
			entity,
			position
		);

		nc().addComponent
		(
			entity,
			nextScene
		);

		nc().addComponent
		(
			entity,
			transform
		);

		nc().addComponent
		(
			entity,
			Component::OriginOffsets{}
		);

		nc().addComponent
		(
			entity,
			originPoint
		);

		return entity;
	}

	Entity text
	(
		const Component::Text& text,
		const Component::ZIndex& zIndex,
		const Component::Position& position
	)
	{
		Entity entity = nc().createEntity();

		nc().addComponent
		(
			entity,
			text
		);

		nc().addComponent
		(
			entity,
			zIndex
		);

		nc().addComponent(
			entity,
			position
		);

		nc().addComponent // unused but required
		(
			entity,
			Component::Transform{}
		);

		return entity;
	}
}

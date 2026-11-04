#include <SFML/Graphics.hpp>

#include <optional>
#include <queue>

#include "../src/Game/Headers/GameManager.hpp"
#include "../src/Game/Headers/Scenes.hpp"

void playingScene()
{
	// game state variables
	sf::Clock clock;
	std::queue<Entity> renderQueue;

	Component::Singleton::TexturesContainer loadedTextures{};

	// entity instantiation
	Entity player = EntityMaker::player
	(
		Component::Drag
		{
			.x = 50.0,
			.y = 50.0,
		},
		Component::Color{ .data = sf::Color::Green },
		Component::ZIndex{ .index = static_cast<int>(Enum::Layer::MAIN) },
		Component::Texture{ .data = Enum::Texture::TEXTURE_PLACEHOLDER },
		Component::Position
		{
			.x = 0.0,
			.y = 0.0,
		},
		Component::Velocity
		{
			.minX = -300.0,
			.minY = -300.0,
			.maxX = 300.0,
			.maxY = 300.0,
		},
		Component::Transform
		{
			.width = 40.0,
			.height = 40.0,
		},
		Component::OriginPoint{ .data = Enum::OriginPoint::CENTER },
		Component::Acceleration
		{
			.x = 500.0,
			.y = 500.0,
		}
	);

	// onstart systems
	Start::loadTextures(loadedTextures);
	Start::loadSprites(loadedTextures);
	Start::setSpriteOrigin();
	Start::setColor();

	while (Asset::window().isOpen())
	{
		DeltaTime dt = clock.restart().asSeconds();

		while (const std::optional event = Asset::window().pollEvent())
		{
			if (event->is<sf::Event::Closed>())
			{
				Asset::window().close();
			}
		}

		// systems
		Control::doPlayerControl
		(
			player,
			dt
		);
		Update::boundaryBounce
		(
			-(Config::SCREEN_WIDTH / 2.0),
			(Config::SCREEN_WIDTH / 2.0),
			-(Config::SCREEN_HEIGHT / 2.0),
			(Config::SCREEN_HEIGHT / 2.0)
		);
		Update::move(dt);
		Update::drag(dt);

		Asset::window().clear();
		// render systems
		Render::doZIndex(renderQueue);
		Render::render
		(
			Asset::window(),
			renderQueue
		);
		Asset::window().display();
	}
}
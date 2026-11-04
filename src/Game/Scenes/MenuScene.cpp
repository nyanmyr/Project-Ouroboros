#include <SFML/Graphics.hpp>

#include <optional>
#include <queue>

#include "../src/Game/Headers/GameManager.hpp"
#include "../src/Game/Headers/Scenes.hpp"

static constexpr double DEFAULT_HOVER_ANIMATION_DURATION = 0.4;
static constexpr double DEFAULT_IDLE_ANIMATION_DURATION = 0.4;
static constexpr double DEFAULT_CLICKED_ANIMATION_DURATION = 0.15;

void menuScene()
{

	// game state variables
	sf::Clock clock;
	std::queue<Entity> renderQueue;

	Component::Singleton::TexturesContainer loadedTextures{};

	Entity title = EntityMaker::text
	(
		Component::Text
		{
			.box = Asset::font(Asset::Font::MAIN),
			.string = "Nacre Engine",
			.size = 64,
			.color = sf::Color::White,
			.format = Enum::TextFormat::CENTER,
		},
		Component::ZIndex{ .index = static_cast<int>(Enum::Layer::TEXT) },
		Component::Position
		{
			.x = 0.0,
			.y = -200.0,
		}
	);

	// entity instantiation
	Entity playButton = EntityMaker::button
	(
		Component::Text
		{
			.box = Asset::font(Asset::Font::MAIN),
			.string = "Play",
			.size = 64,
			.color = sf::Color::White,
			.format = Enum::TextFormat::CENTER,
		},
		Component::Color{ .data = sf::Color::Red },
		Component::Button
		{
			.hoverAnimationDuration = DEFAULT_HOVER_ANIMATION_DURATION,
			.idleAnimationDuration = DEFAULT_IDLE_ANIMATION_DURATION,
			.clickedAnimationDuration = DEFAULT_CLICKED_ANIMATION_DURATION,
			
		},
		Component::ZIndex{ .index = static_cast<int>(Enum::Layer::UI) },
		Component::Texture{ .data = Enum::Texture::TEXTURE_PLACEHOLDER },
		Component::Position
		{
			.x = 0.0,
			.y = 0.0,
		},
		Component::NextScene{ .next = Scene::PLAYING },
		Component::Transform
		{
			.width = 200.0,
			.height = 100.0,
		},
		Component::OriginPoint{ .data = Enum::OriginPoint::CENTER }
	);

	// onstart systems
	Start::setText();
	Start::setTextOrigin();

	Start::loadTextures(loadedTextures);
	Start::loadSprites(loadedTextures);
	Start::setSpriteOrigin();
	Start::setColor();

	while (Asset::window().isOpen())
	{
		DeltaTime dt = clock.restart().asSeconds();

		const auto& pixelPos = sf::Mouse::getPosition(Asset::window());
		const auto& worldPos = Asset::window().mapPixelToCoords(pixelPos);

		while (const std::optional event = Asset::window().pollEvent())
		{
			if (event->is<sf::Event::Closed>())
			{
				Asset::window().close();
			}

			if (const auto& mousePressed = event->getIf<sf::Event::MouseButtonPressed>())
			{
				if (mousePressed->button == sf::Mouse::Button::Left)
				{
					nc().call<Event::MouseClick>
						(
							Event::MouseClick
							{
								sf::Vector2f
								{
									worldPos.x,
									worldPos.y
								}
							}
						);
				}
			} else if (event->is<sf::Event::MouseMovedRaw>() ||
				event->is<sf::Event::MouseLeft>() ||
				event->is<sf::Event::MouseEntered>())
			{
				nc().call<Event::MouseMoved>
					(
						Event::MouseMoved
						{
							sf::Vector2f
							{
								worldPos.x,
								worldPos.y
							}
						}
					);
			}
		}

		// update systems
		Update::doButtonTimer(dt);
		Update::applyButtonHoverAnimation();
		Update::applyButtonIdleAnimation();
		Update::applyButtonClickedAnimation();
		Update::enableButtonNextScene();

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
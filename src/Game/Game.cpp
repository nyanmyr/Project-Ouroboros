#include <SFML/Graphics.hpp>

#include "Headers/GameManager.hpp"
#include "Headers/Scenes.hpp"

#include <iostream>

using sf::RenderWindow;
using sf::VideoMode;

constexpr int SCREEN_WIDTH = 800;
constexpr int SCREEN_HEIGHT = 600;

constexpr int MAX_FPS = 60;

void main() {
	RenderWindow window(VideoMode(SCREEN_WIDTH, SCREEN_HEIGHT), "Nacre Engine", sf::Style::Close);
	window.setFramerateLimit(MAX_FPS);

	EntityManager& em = EntityManager::getInstance();
	ComponentManager& cm = ComponentManager::getInstance();

	// components registration
	cm.registerComponent<CPosition>();
	cm.registerComponent<CShape>();

	playScene(window, Scene::MENU);
}
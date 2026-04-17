#ifndef SCENES_HPP
#define SCENES_HPP

#include <SFML/Graphics.hpp>

#include <stdexcept>

enum Scene {
	MENU,
	PLAYING,
	PAUSED,
	GAME_OVER
};

void playScene(sf::RenderWindow& window, Scene scene);
void MenuScene(sf::RenderWindow& window);
void PlayingScene(sf::RenderWindow& window);


#endif
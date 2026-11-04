#ifndef SCENES_HPP
#define SCENES_HPP

#include <SFML/Graphics.hpp>

enum class Scene
{
	MENU,
	PLAYING,
	CHARACTER_CREATION
};

void playScene(Scene scene);
void menuScene();
void playingScene();
void characterCreation();


#endif
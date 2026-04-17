#include "../Headers/Scenes.hpp"
#include <stdexcept>

void playScene(sf::RenderWindow& window, Scene scene) {
    switch (scene) {
    case MENU:
        MenuScene(window);
        break;
    case PLAYING:
        PlayingScene(window);
        break;
    case PAUSED:
        break;
    case GAME_OVER:
        break;
    default:
        throw std::runtime_error("Scene does not exist.");
        break;
    }
}

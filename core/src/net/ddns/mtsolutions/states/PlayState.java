package net.ddns.mtsolutions.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import net.ddns.mtsolutions.FlappyDemo;
import net.ddns.mtsolutions.sprites.Bird;
import net.ddns.mtsolutions.sprites.Tube;

public class PlayState extends State {
	public static final int TUBE_SPACING = 125;
	public static final int TUBE_COUNT = 4;

	private Bird bird;
	private Texture background;

	private Array<Tube> tubes;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		bird = new Bird(50, 300);
		camera.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
		background = new Texture("bg.png");
		tubes = new Array<Tube>();

		for (int i = 0; i < TUBE_COUNT; i++) {
			tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
		}
	}

	@Override
	protected void handleInput() {
		if (Gdx.input.justTouched()) {
			bird.jump();
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
		bird.update(dt);
		camera.position.x = bird.getPosition().x + 80;

		for (Tube tube : tubes) {
			if (camera.position.x - (camera.viewportWidth / 2)
					> tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
				tube.reposition(tube.getPosTopTube().x
						+ ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
			}

			if (tube.collides(bird.getBounds())) {
				gsm.set(new PlayState(gsm));
			}
		}
		camera.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(background, camera.position.x - (camera.viewportWidth / 2), 0);
		sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
		for (Tube tube : tubes) {
			sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
			sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
		}
		sb.end();
	}

	@Override
	public void dispose() {

	}
}

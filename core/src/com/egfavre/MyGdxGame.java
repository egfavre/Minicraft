package com.egfavre;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.sun.tools.doclint.Entity.delta;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	float time, x, y, xv, yv, ratX, ratXv;
	TextureRegion down, up, right, left, groundTiles, ratStand;
	Texture pineTree, sky, youDied;
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final int FINAL_WIDTH = WIDTH * 5;
	static final int FINAL_HEIGHT = HEIGHT * 5;
	static final float MAX_VELOCITY = 300;
	static final float MAX_JUMP_VELOCITY = 2000;
	static final float DECELERATION = 0.95f;
	static final int GRAVITY = -50;
	static final int RIGHT_EDGE = 600;
	boolean canJump, faceRight = true;
	Animation walk, ratAttack;
	int i = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture tiles = new Texture("tiles.png");
		Texture ground = new Texture("groundtiles.png");
		Texture rat = new Texture("cr.png");
		pineTree = new Texture("PineTree.png");
		sky = new Texture("Daytime_Background_1024x800.png");
		youDied = new Texture("youDied.png");
		TextureRegion[][] grid = TextureRegion.split(tiles, WIDTH, HEIGHT);
		TextureRegion[][] grid2 = TextureRegion.split(ground, WIDTH, HEIGHT);
		TextureRegion[][] grid3 = TextureRegion.split(rat, 62, 55);
		groundTiles = grid2[0][0];
		down = grid[6][0];
		up = grid[6][1];
		right = grid[6][3];
		ratStand = grid3[0][0];
		ratStand.flip(true, false);
		left = new TextureRegion(right);
		left.flip(true, false);
		walk = new Animation(0.2f, grid[6][0], grid[6][2]);
		ratAttack = new Animation(0.2f, grid3[0][0], grid3[0][1], grid3[0][2]);
		grid3[0][2].flip(true, false);
		grid3[0][4].flip(true, false);
	}

		@Override
		public void render () {
			move();
			time += Gdx.graphics.getDeltaTime();
			TextureRegion img;
			wrapScreen();

			if (y > 0) {
				img = up;
			} else if (xv != 0) {
				img = walk.getKeyFrame(time, true);
			} else {
				img = left;
			}

			ratStand = ratAttack.getKeyFrame(time, true);
			Gdx.gl.glClearColor(0f, .5f, 1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();
			ratX = RIGHT_EDGE - i;
			batch.draw(sky, 0, 50, FINAL_WIDTH * 10, FINAL_HEIGHT * 6);
			batch.draw(groundTiles, 0, 0, FINAL_WIDTH * 10, FINAL_HEIGHT * 2);
			batch.draw(pineTree, 50, 50, FINAL_WIDTH * 2.5f, FINAL_HEIGHT * 2.5f);
			batch.draw(pineTree, 250, 100, FINAL_WIDTH * 2.5f, FINAL_HEIGHT * 2.5f);
			batch.draw(ratStand, ratX, 0);

			if (faceRight) {
			batch.draw(img, x, y, FINAL_WIDTH, FINAL_HEIGHT);}
			else {
			batch.draw(img, x + FINAL_WIDTH, y, -FINAL_WIDTH, FINAL_HEIGHT);
			}

			batch.draw(pineTree, 200, 0, FINAL_WIDTH * 2.5f, FINAL_HEIGHT * 2.5f);
			batch.draw(pineTree, 400, 2, FINAL_WIDTH * 2.5f, FINAL_HEIGHT * 2.5f);
			contact();
			batch.end();
			i++;


		}

	public void contact(){
		if (x == ratX){
			batch.draw(youDied, 0, 0);
		}
	}

	public void move(){
		if(Gdx.input.isKeyPressed(Input.Keys.UP) && canJump){
			yv = MAX_JUMP_VELOCITY;
			canJump = false;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			yv = -MAX_VELOCITY;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			xv = MAX_VELOCITY * 2;
			faceRight = true;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			xv = MAX_VELOCITY;
			faceRight = true;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			xv = -MAX_VELOCITY * 2;
			faceRight = true;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			xv = -MAX_VELOCITY;
			faceRight = false;
		}
		yv += GRAVITY;

		float delta = Gdx.graphics.getDeltaTime();
		y += yv * delta;
		x += xv * delta;

		yv = decelerate(yv);
		xv = decelerate(xv);

		if (y<0) {
			y = 0;
			canJump = true;
		}

	}

	public float decelerate(float velocity){
		velocity *= DECELERATION;
		if (Math.abs(velocity) < 1) {
			velocity = 0;
		}
		return velocity;
	}

	public void wrapScreen() {
		if (x > 600) {
			x = 0;
		}
		if (x < -0) {
			x = 600;
		}
	}
}

package com.egfavre;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	float time;
	TextureRegion down;
	TextureRegion up;
	TextureRegion right;
	TextureRegion left;
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final int FINAL_WIDTH = WIDTH * 5;
	static final int FINAL_HEIGHT = HEIGHT * 5;
	float x, y, xv, yv;
	boolean canJump, faceRight = true;
	static final float MAX_VELOCITY = 300;
	static final float MAX_JUMP_VELOCITY = 2000;
	static final float DECELERATION = 0.95f;
	static final int GRAVITY = -50;
	Animation walk;


	@Override
	public void create () {
		batch = new SpriteBatch();
//add player tiles
		Texture tiles = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(tiles, WIDTH, HEIGHT);
		down = grid[6][0];
		up = grid[6][1];
		right = grid[6][3];
		left = new TextureRegion(right);
		left.flip(true, false);
		walk = new Animation(0.2f, grid[6][0], grid[6][2]);
	}

	@Override
	public void render () {

		move();
		time += Gdx.graphics.getDeltaTime();
		TextureRegion img;
		if (x > 600){
			x = 0;
		}
		if (x < -0){
			x = 600;
		}

		if (y>0) {
			img = up;
		}
		else if (xv != 0) {
			img = walk.getKeyFrame(time, true);
		}
		else {
			img = left;
		}



		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if (faceRight) {
			batch.draw(img, x, y, FINAL_WIDTH, FINAL_HEIGHT);
		}
		else {
			batch.draw(img, x + FINAL_WIDTH, y, -FINAL_WIDTH, FINAL_HEIGHT);
		}
		batch.end();
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
}

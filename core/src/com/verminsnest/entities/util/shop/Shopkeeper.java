package com.verminsnest.entities.util.shop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.verminsnest.core.management.data.RuntimeData;
import com.verminsnest.core.management.ids.Indentifiers;
import com.verminsnest.core.management.ids.Qualifier;
import com.verminsnest.entities.util.UtilEntity;

public class Shopkeeper extends UtilEntity{

	public Shopkeeper(int[] pos) {
		super(pos, Indentifiers.ASSETMANAGER_SHOPKEEPER,Qualifier.RENDER_LAYER_MID);
	}

	@Override
	public void init() {
		Texture idleSheet = RuntimeData.getInstance().getTexture("textures/characters/shopkeeper/Rabbit-Idle.png");
		TextureRegion[][] temp = TextureRegion.split(idleSheet, idleSheet.getWidth()/2, idleSheet.getHeight());
		TextureRegion[] frames = new TextureRegion[temp[0].length];

		shadow = RuntimeData.getInstance().getTexture("textures/shadows/Shadow-Long-M.png");
		yShadowOffset = -5;
		xShadowOffset = -2;
		for (int i = 0; i < temp[0].length; i++) {
			frames[i] = temp[0][i];
		}
		currentAni = new Animation<TextureRegion>(1f, frames);
		setSize(idleSheet.getWidth()/2, idleSheet.getHeight());
		state = Indentifiers.STATE_IDLE;
	}

	@Override
	public void setCurrentAni(int animationKey) {
		//Not needed
	}

	@Override
	public void update(float delta) {
		internalStateTime+= delta;
	}
}

package com.verminsnest.entities.playables;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.verminsnest.core.engine.shaders.Shader;
import com.verminsnest.core.management.data.RuntimeData;
import com.verminsnest.core.management.ids.Indentifiers;
import com.verminsnest.core.management.ids.Qualifier;
import com.verminsnest.entities.Entity;
import com.verminsnest.misc.entities.Inventory;

public abstract class Playable extends Entity {
	
	protected Animation<TextureRegion> frontWalkAni;
	protected Animation<TextureRegion> backWalkAni;
	protected Animation<TextureRegion> rightWalkAni;
	protected Animation<TextureRegion> leftWalkAni;
	protected Animation<TextureRegion> idleAni;
	
	protected int level;
	protected int killCount;
	protected int killLimit;
	protected int skillPoints;
	protected Inventory inv;
	
	protected int speed;
	protected int agility;
	protected int strength;
	protected int health;
	protected int maxHealth;
	
	protected float attackCooldown;
	protected String attackIconPath;
	
	private char currentKey;
	private char prevKey;
	
	private Vector2 topLeft;
	private Vector2 bottomLeft;
	private Vector2 topRight;
	private Vector2 bottomRight;
	
	private int[] sizeModifier;
	
	public Playable(int textureID,int[] position, int speed, int dmg, int agi, int health){
		super(position,textureID, Qualifier.RENDER_LAYER_MID);
		setSpeed(speed);
		setHealth(health);
		setMaxHealth(health);
		setStrength(dmg);
		setAgility(agi);
		
		inv = new Inventory();
		
		skillPoints = 2;
		killCount = 0;
		killLimit = 10;
		level = 5;
		
		prevKey = '-';
		currentKey = '-';
		
		shadow = RuntimeData.getInstance().getTexture("textures/shadows/Shadow-M.png");
		
		init();
		this.setCurrentAni(Indentifiers.STATE_IDLE);
		this.setSize(currentAni.getKeyFrame(0).getRegionWidth(),currentAni.getKeyFrame(0).getRegionHeight());
		
		switch(RuntimeData.getInstance().getGame().getConfig().getResolution()[0]){
		case 1920:
			sizeModifier = new int[]{1,1};
			break;
		case 1280:
			sizeModifier = new int[]{1280/1920,720/1080};
			break;
		case 852:
			sizeModifier = new int[]{852/1920,420/1080};
			break;
		}
		
		topLeft = new Vector2((RuntimeData.getInstance().getGame().getConfig().getResolution()[0]+size[0]*sizeModifier[0])/2,(RuntimeData.getInstance().getGame().getConfig().getResolution()[1]+size[1]*sizeModifier[1])/2).nor();
		topLeft.setAngle(135);
		bottomLeft = new Vector2((RuntimeData.getInstance().getGame().getConfig().getResolution()[0]+size[0]*sizeModifier[0])/2,(RuntimeData.getInstance().getGame().getConfig().getResolution()[1]+size[1]*sizeModifier[1])/2).nor();
		bottomLeft.setAngle(-135);
		topRight = new Vector2((RuntimeData.getInstance().getGame().getConfig().getResolution()[0]+size[0]*sizeModifier[0])/2,(RuntimeData.getInstance().getGame().getConfig().getResolution()[1]+size[1]*sizeModifier[1])/2).nor();
		topRight.setAngle(45);
		bottomRight = new Vector2((RuntimeData.getInstance().getGame().getConfig().getResolution()[0]+size[0]*sizeModifier[0])/2,(RuntimeData.getInstance().getGame().getConfig().getResolution()[1]+size[1]*sizeModifier[1])/2).nor();
		bottomRight.setAngle(-45);
	}
	
	public void attack(float delta, int direction){
		if (attackCooldown > 1/(agility*0.2)) {
			attackAction(delta, direction);
		}
	}
	
	public abstract void init();
	public abstract void attackAction(float delta, int direction);

	public void update(float delta){
		if(isForced){
			if(forceTimer < 0)setForced(false,0,0);
			else RuntimeData.getInstance().getEntityDamageSystem().knockBack(this, forceDirection);
		}else{
			if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
				prevKey = currentKey;
				currentKey = 'S';
			}
			// D Pressed
			if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
				prevKey = currentKey;
				currentKey = 'D';
			}
			// A Pressed
			if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
				prevKey = currentKey;
				currentKey = 'A';
			}
			// W Pressed
			if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
				prevKey = currentKey;
				currentKey = 'W';
			}
			//Calculates proper movement
			switch (currentKey) {
			case 'W':
				if (!Gdx.input.isKeyPressed(Input.Keys.W)) {
					if (prevKey != '-') {
						currentKey = prevKey;
						prevKey = '-';
					} else {
						currentKey = '-';
					}
				}
				if(RuntimeData.getInstance().getMovmentSystem().moveTop(this, this.getSpeed(), null)){
					RuntimeData.getInstance().getAudioManager().loopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				}else{
					RuntimeData.getInstance().getAudioManager().stopLoopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				}
				this.setCurrentAni(Indentifiers.STATE_WALK_NORTH);
				break;
			case 'D':
				if (!Gdx.input.isKeyPressed(Input.Keys.D)) {
					if (prevKey != '-') {
						currentKey = prevKey;
						prevKey = '-';
					} else {
						currentKey = '-';
					}
				}
				if(RuntimeData.getInstance().getMovmentSystem().moveRight(this, this.getSpeed(), null)){
					RuntimeData.getInstance().getAudioManager().loopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				}else{
					RuntimeData.getInstance().getAudioManager().stopLoopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				}
				this.setCurrentAni(Indentifiers.STATE_WALK_EAST);
				break;
			case 'S':
				if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
					if (prevKey != '-') {
						currentKey = prevKey;
						prevKey = '-';
					} else {
						currentKey = '-';
					}
				}
				if(RuntimeData.getInstance().getMovmentSystem().moveDown(this, this.getSpeed(), null)){
					RuntimeData.getInstance().getAudioManager().loopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				}else{
					RuntimeData.getInstance().getAudioManager().stopLoopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				}
				this.setCurrentAni(Indentifiers.STATE_WALK_SOUTH);
				break;
			case 'A':
				if (!Gdx.input.isKeyPressed(Input.Keys.A)) {
					if (prevKey != '-') {
						currentKey = prevKey;
						prevKey = '-';
					} else {
						currentKey = '-';
					}
				}
				if(RuntimeData.getInstance().getMovmentSystem().moveLeft(this, this.getSpeed(), null)){
					RuntimeData.getInstance().getAudioManager().loopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				}else{
					RuntimeData.getInstance().getAudioManager().stopLoopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				}
				this.setCurrentAni(Indentifiers.STATE_WALK_WEST);
				break;
			case '-':
				RuntimeData.getInstance().getAudioManager().stopLoopSoundEffect("audio/sounds/walking/Walking-On-Stone.mp3");
				this.setCurrentAni(Indentifiers.STATE_IDLE);
				prevKey = '-';
			}		
			
			//Attacking
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				float mouseX =RuntimeData.getInstance().getMousePosInGameWorld().x;
				float mouseY =RuntimeData.getInstance().getGame().getConfig().getResolution()[1]-RuntimeData.getInstance().getMousePosInGameWorld().y;
				Vector2 mouseVector = new Vector2(mouseX-(RuntimeData.getInstance().getGame().getConfig().getResolution()[0]+size[0]*sizeModifier[0])/2,mouseY-(RuntimeData.getInstance().getGame().getConfig().getResolution()[1]+size[1]*sizeModifier[1])/2).nor();
				if(mouseVector.y - bottomLeft.y >= 0 && mouseVector.y - topLeft.y <= 0 && mouseX < (RuntimeData.getInstance().getGame().getConfig().getResolution()[0]+size[0]*sizeModifier[0])/2){
					this.attack(delta, Indentifiers.DIRECTION_WEST);
				}else if(mouseVector.y - bottomRight.y >= 0 && mouseVector.y - topRight.y <= 0&& mouseX > (RuntimeData.getInstance().getGame().getConfig().getResolution()[0]+size[0]*sizeModifier[0])/2){
					this.attack(delta, Indentifiers.DIRECTION_EAST);
				}if(mouseVector.x - topRight.x <= 0 && mouseVector.x - topLeft.x >= 0 && mouseY > (RuntimeData.getInstance().getGame().getConfig().getResolution()[1]+size[1]*sizeModifier[1])/2){
					this.attack(delta, Indentifiers.DIRECTION_NORTH);
				}else if(mouseVector.x - bottomRight.x <= 0 && mouseVector.x - bottomLeft.x >= 0 && mouseY < (RuntimeData.getInstance().getGame().getConfig().getResolution()[1]+size[1]*sizeModifier[1])/2){
					this.attack(delta, Indentifiers.DIRECTION_SOUTH);
				}	
			}
			//Item activation
			if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
				if(inv.getItem() != null && !inv.getItem().isPassiv()
						&& inv.getCooldown() > inv.getItem().getBaseCooldown()*1/(agility*0.15)){
					inv.getItem().activate();
					inv.setCooldown(0);
				}
			}
			
			//Item drop
			if(Gdx.input.isKeyJustPressed(Input.Keys.Q)){
				if(inv.getItem() != null){
					ArrayList<int[]> allCorners = new ArrayList<>();
					//boolean dropped = false;
					allCorners.add(pos);
					allCorners.add(new int[]{pos[0]+size[0],pos[1]});
					allCorners.add(new int[]{pos[0],pos[1]+size[1]});
					allCorners.add(new int[]{pos[0]+size[0],pos[1]+size[1]});
					ArrayList<int[]> allMapCorners = this.getMapPos();
					for(int i = 0; i < allCorners.size(); i++){
						if(RuntimeData.getInstance().getEntityManager().placeOnTile(allMapCorners.get(i), allCorners.get(i), inv.getItem())){
							RuntimeData.getInstance().getAudioManager().playSoundEffect("audio/sounds/items/Item-Pick-Drop.mp3");
							break;
						};
					}
					//TODO dropped == false message
				}
			}
			//Eat food (heal)
			if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
				if(health < maxHealth && inv.getFoodCount() > 0) {
					inv.setFoodCount(inv.getFoodCount()-1);
					this.health += this.maxHealth/10;
					if(health > maxHealth) {
						health = maxHealth;
					}
					RuntimeData.getInstance().getAudioManager().playSoundEffect("audio/sounds/items/Food-Eating.mp3");
				}
				//TODO is full life message
			}
		}
		
		Shader.getInstance().addPosition(this.pos);
		attackCooldown += delta;
		inv.update(delta);
		forceTimer -= delta;
		internalStateTime += delta;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getAgility() {
		return agility;
	}

	public void setAgility(int agility) {
		this.agility = agility;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}
	
	@Override
	public void setCurrentAni(int animationKey) {
		switch (animationKey){
		case Indentifiers.STATE_WALK_SOUTH:
			currentAni = frontWalkAni;
			state = Indentifiers.STATE_WALK_SOUTH;
			break;
		case Indentifiers.STATE_WALK_NORTH:
			currentAni = backWalkAni;
			state = Indentifiers.STATE_WALK_NORTH;
			break;
		case Indentifiers.STATE_WALK_WEST:
			currentAni = leftWalkAni;
			state = Indentifiers.STATE_WALK_WEST;
			break;
		case Indentifiers.STATE_WALK_EAST:
			currentAni = rightWalkAni;
			state = Indentifiers.STATE_WALK_EAST;
			break;
		case Indentifiers.STATE_IDLE:
			currentAni = idleAni;
			state = Indentifiers.STATE_IDLE;
			break;
		}
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	public String getAttackIcon() {
		return attackIconPath;
	}
	
	public float[] getAttackDetails(){
		if(attackCooldown >1/(agility*0.2))return new float[]{(float) (1/(agility*0.2)) ,(float) (1/(agility*0.2))};
		else return new float[]{attackCooldown ,(float) (1/(agility*0.2))};
	}

	public float[] getItemDetails(){
		if(inv.getItem() == null) return null;
		return new float[]{inv.getCooldown(),(float) (inv.getItem().getBaseCooldown()*1/(agility*0.15))};
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getSkillPoints(){
		return skillPoints;
	}
	
	public void setSkilPoints(int skillPoints){
		this.skillPoints = skillPoints;
	}
	
	public void updateKills(){
		if(killCount+1 == killLimit){
			killCount = 0;
			killLimit += 5;
			level++;
			skillPoints += 2;
			RuntimeData.getInstance().getAudioManager().playSoundEffect("audio/sounds/general/Level-Up.mp3");
		}else{
			killCount++;
		}
	}
	
	public int[] getLevelData(){
		return new int[]{level, killCount, killLimit};
	}
}

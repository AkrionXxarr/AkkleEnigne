package game.scenes.test;

import core.math.vector.Vector3f;
import core.resource.graphics.Bitmap;
import core.resource.graphics.TileSet;
import core.utility.Converter;
import game.component.graphics.SpriteRenderer;
import game.component.physics.CircleCollider;
import game.component.test.BasicMovement;
import game.component.test.Bounce;
import game.engine.Game;
import game.engine.RenderContext;
import game.object.base.GameObject;
import game.scenes.Scene;

public class TestScene1 extends Scene 
{
	private final GameObject root = new GameObject("root");
	
	public TestScene1() 
	{
		super("TestScene1");
	}
	
	@Override
	public void update(float deltaTime) 
	{	
		root.update(deltaTime);
	}

	@Override
	public void render(RenderContext renderContext) 
	{
		root.draw(renderContext);
	}

	@Override
	public void activate(Game game) 
	{		
		root.setGameRef(game);
		
		GameObject testSprite1 = new GameObject("Test");
		GameObject testSprite2 = new GameObject("Test");
		GameObject testSprite3 = new GameObject("Test");
		GameObject testSprite1orbit = new GameObject("Test");
		GameObject orbitOrbiter = new GameObject("Test");
		GameObject orbitOrbiter2 = new GameObject("Test");
		
		root.addChild(testSprite1);
		root.addChild(testSprite2);
		root.addChild(testSprite3);
		
		Bitmap bmp1 = new Bitmap("./res/bitmaps/", "TestTileset.bmp");
		bmp1.useColorMask(Converter.RGBToInt(255, 0, 255));
		
		TileSet tileSet = new TileSet(bmp1, 32, 32, 1, 1);
		
		testSprite1.addComponent(new SpriteRenderer("1", new Bitmap(tileSet.getTile(1, 6))));
		testSprite2.addComponent(new SpriteRenderer("2", new Bitmap(tileSet.getTile(1, 6))));
		testSprite3.addComponent(new SpriteRenderer("3", new Bitmap(tileSet.getTile(1, 6))));
		testSprite1orbit.addComponent(new SpriteRenderer("1 orbit", new Bitmap(tileSet.getTile(3, 3))));
		orbitOrbiter.addComponent(new SpriteRenderer("orbit orbiter", new Bitmap(tileSet.getTile(2,  5))));
		orbitOrbiter2.addComponent(new SpriteRenderer("orbit orbiter2", new Bitmap(tileSet.getTile(7,  7))));
		
		testSprite1.local.pos = new Vector3f(30, 30, 0);
		testSprite2.local.pos = new Vector3f(20, 49, 0);
		testSprite3.local.pos = new Vector3f(60, 50, 0);
		testSprite1orbit.local.pos = new Vector3f(-32, 0, 10);
		orbitOrbiter.local.pos = new Vector3f(-32, 0, 11);
		orbitOrbiter2.local.pos = new Vector3f(0, 0, 12);
		
		testSprite1.addComponent(new CircleCollider(16, 16, 16));
		testSprite2.addComponent(new CircleCollider(16, 16, 16));
		testSprite3.addComponent(new CircleCollider(16, 16, 16));
		testSprite1orbit.addComponent(new BasicMovement("orbit", 1, 1, 0, 32));
		orbitOrbiter.addComponent(new BasicMovement("orbitOrbiter", 1, 0, 0, 32));
		orbitOrbiter2.addComponent(new BasicMovement("orbitOrbiter2", 0, 1, 0, 32));
		
		testSprite1.addComponent(new Bounce("Bounce 1", 1.9f, 1.1345f, 30));
		testSprite2.addComponent(new Bounce("Bounce 2", 1, 0.1234f, 30));
		testSprite3.addComponent(new Bounce("Bounce 3", 1, 0, 30));
		
		testSprite1.addChild(testSprite1orbit);
		testSprite1orbit.addChild(orbitOrbiter);
		orbitOrbiter.addChild(orbitOrbiter2);
		
		root.start();
	}

	@Override
	public void deactivate(Game game) 
	{
		root.stop();
	}
}

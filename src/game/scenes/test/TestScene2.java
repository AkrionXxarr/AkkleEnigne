package game.scenes.test;

import java.util.Random;

import game.component.graphics.BasicBackground;
import game.component.graphics.SpriteRenderer;
import game.component.physics.BoxCollider;
import game.component.physics.CircleCollider;
import game.component.test.BasicKeyMovement;
import game.component.test.Bounce;
import game.engine.Game;
import game.engine.RenderContext;
import game.object.base.GameObject;
import game.scenes.Scene;
import core.math.vector.Vector3f;
import core.resource.audio.AudioManager;
import core.resource.audio.AudioStream;
import core.resource.graphics.Bitmap;
import core.resource.graphics.TileSet;
import core.utility.Converter;

public class TestScene2 extends Scene
{
	private final GameObject root = new GameObject("root");
	
	public TestScene2() 
	{
		super("TestScene2");
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

		GameObject background = new GameObject("Background");
		GameObject box1 = new GameObject("box1");
		GameObject box2 = new GameObject("box2");
		GameObject box3 = new GameObject("box3");
		GameObject circle1 = new GameObject("circle1");
		
		root.addChild(background);
		root.addChild(box1);
		root.addChild(box2);
		root.addChild(box3);
		root.addChild(circle1);
		
		Bitmap bmp1 = new Bitmap("./res/bitmaps/", "TestTileset.bmp");
		bmp1.useColorMask(Converter.RGBToInt(255, 0, 255));
		
		TileSet tileSet = new TileSet(bmp1, 32, 32, 1, 1);
		
		background.addComponent(new BasicBackground("Background", 0, 0, game.getDisplay().getWidth(), game.getDisplay().getHeight(), -1, 255, 0, 255));

		box1.addComponent(new SpriteRenderer("1", new Bitmap(tileSet.getTile(2, 2))));
		box2.addComponent(new SpriteRenderer("2", new Bitmap(tileSet.getTile(1, 2))));
		box3.addComponent(new SpriteRenderer("3", new Bitmap(tileSet.getTile(3, 3))));
		circle1.addComponent(new SpriteRenderer("4", new Bitmap(tileSet.getTile(1, 6))));
		
		box1.local.pos = new Vector3f(90, 20, 0);
		box2.local.pos = new Vector3f(10, 49, 0);
		box3.local.pos = new Vector3f(80, 60, 0);
		circle1.local.pos = new Vector3f(10, 10, 0);

		box1.addComponent(new BoxCollider(32, 32, 0, 0));
		box2.addComponent(new BoxCollider(32, 32, 0, 0));
		box3.addComponent(new BoxCollider(24, 24, 3, 3));
		circle1.addComponent(new CircleCollider(16, 16, 16));

		box1.addComponent(new Bounce("Bounce 1", 1.9f, 1.1345f, 100));
		box2.addComponent(new Bounce("Bounce 2", 1, 0.1234f, 100));
		box3.addComponent(new Bounce("Bounce 3", 1, 0, 100));
		//circle1.addComponent(new Bounce("Bounce 4", 1, 1, 100));
		circle1.addComponent(new BasicKeyMovement(300));
		
		Random rand = new Random();
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				GameObject go = new GameObject("AutoCircle" + i);
				root.addChild(go);
				
				go.addComponent(new SpriteRenderer("AutoCircle" + i + j, new Bitmap(tileSet.getTile(1, 6))));
				go.local.pos = new Vector3f(100 + i * 50, 100 + j * 50, 0);
				
				go.addComponent(new CircleCollider(16, 16, 16));
				
				go.addComponent(new Bounce("AutoBounce" + i + j, rand.nextFloat(), rand.nextFloat(), 100));
			}
		}
		
		AudioManager.play(new AudioStream("./res/music/", "Opelucid_City_Black.wav"));
		root.start();
	}

	@Override
	public void deactivate(Game game) 
	{
		root.stop();
	}
}

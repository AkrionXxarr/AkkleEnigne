package game.component.graphics;

import core.math.vector.Vector3f;
import core.resource.graphics.Bitmap;
import core.utility.Converter;
import game.component.base.GameComponent;
import game.engine.RenderContext;

public class SpriteRenderer extends GameComponent 
{
	public static final GameComponent.Type type = new GameComponent.Type(SpriteRenderer.class.getName());
	
	public Bitmap sprite;
	public int colorModulation = Converter.ARGBToInt(255, 255, 255, 255);
	public Vector3f offset = new Vector3f(0, 0, 0);
	
	public SpriteRenderer(String name, Bitmap sprite) 
	{
		super(name);
		
		this.sprite = sprite;
	}
	
	public SpriteRenderer(String name, Bitmap sprite, Vector3f offset)
	{
		super(name);
		
		this.sprite = sprite;
		this.offset = offset;
	}
	
	@Override
	public void onAdd()
	{
	}

	@Override
	public void onRemove()
	{
	}

	@Override
	public void start() 
	{
	}
	
	@Override
	public void stop()
	{
	}

	@Override
	public void update(float deltaTime) 
	{
	}

	@Override
	public void draw(RenderContext renderContext) 
	{
		renderContext.draw(
				sprite.getBuffer(), 
				sprite.getWidth(), 
				sprite.getHeight(), 
				colorModulation, 
				new Vector3f(
						parent.world.pos.x + offset.x, 
						parent.world.pos.y + offset.y, 
						parent.world.pos.z + offset.z));
	}
}

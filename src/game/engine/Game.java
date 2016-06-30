package game.engine;

import java.awt.event.WindowEvent;

import core.engine.BaseEngine;
import core.event.EventManager;
import core.event.test.TestFocusListener;
import core.input.InputEngine;
import core.input.InputEvent;
import core.resource.graphics.CharacterSet;
import core.utility.console.events.ConsoleMessage;
import core.utility.console.listeners.ConsoleMessageListener;
import core.utility.output.Output;
import game.scenes.Scene;

// Sets up all the systems and manages the main loop.
//
// A completely different class can be used, if desired, so long as it inherits from BaseEngine.
//
public class Game extends BaseEngine 
{
	private boolean running = false;
	
	private Scene activeScene = null;
	private Scene nextScene = null;
	
	private final RenderContext renderContext;
	private final Physics physics;
	
	private final EventManager eventManager;
	private final InputEngine inputEngine;
	private final Output output;
	public final CharacterSet charSet;
	
	private final ConsoleMessageListener consoleMessageListener;
	
	private final TestFocusListener focusListener;
	
	boolean processingEvents;
	
	public Game(int width, int height, int scale)
	{
		super("Game", width, height, scale);
		
		renderContext = new RenderContext(super.getDisplay());
		physics = new Physics();
		
		eventManager = new EventManager();
		
		inputEngine = new InputEngine(this);
		output = new Output("./output/");
		
		charSet = new CharacterSet(
				"./res/bitmaps/", 
				"CharacterSet8p.bmp", 
				"./res/bitmaps/", 
				"CharacterSet16p.bmp");
		
		consoleMessageListener = new ConsoleMessageListener(this.toString() + " console message listener");
		
		focusListener = new TestFocusListener();
		
		EventManager.register(consoleMessageListener, ConsoleMessage.log);
		EventManager.register(consoleMessageListener, ConsoleMessage.warning);
		EventManager.register(consoleMessageListener, ConsoleMessage.error);
		
		EventManager.register(focusListener, InputEvent.Focus.gained);
		EventManager.register(focusListener, InputEvent.Focus.lost);
	}

	@Override
	public void initialize() 
	{
		start();
	}

	@Override
	public void start() 
	{	
		if (running)
			return;
		
		output.start();
		
		running = true;
		run();
	}
	
	@Override
	public void stop()
	{
		if (!running)
			return;
		
		output.stop();
		
		running = false;
	}

	@Override
	public void run() 
	{	
		float deltaTime = time.deltaTime();
		
		int frameCounter = 0;
		float frameTimer = 0;
		
		while (running)
		{	
			if (nextScene != null)
			{
				setActiveScene(nextScene);
				nextScene = null;
			}
			
			renderContext.clearZBuffer();
			
			inputEngine.update();
			
			eventManager.process(10);
			
			deltaTime = time.deltaTime();
			
			frameCounter++;
			frameTimer += deltaTime;
			
			if (frameTimer > 1.0f)
			{	
				System.out.println("fps: " + frameCounter);
				
				frameCounter = 0;
				frameTimer = 0;
			}
			
			update(deltaTime);
			render();
		}
		
		display.getFrame().dispatchEvent(new WindowEvent(display.getFrame(), WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void update(float deltaTime) 
	{	
		physics.update(this);
		activeScene.update(deltaTime);
	}

	@Override
	public void render() 
	{
		activeScene.render(renderContext);
		
		display.slam();
	}
	
	public void setActiveScene(Scene scene)
	{
		if (activeScene != null)
			activeScene.deactivate(this);
		
		activeScene = scene;
		activeScene.activate(this);
	}
	
	public void setNextScene(Scene scene)
	{
		nextScene = scene;
	}
	
	public final RenderContext getRenderContext()
	{
		return renderContext;
	}
	
	public final Physics getPhysicsEngine()
	{
		return physics;
	}
	
	public final InputEngine getInputEngine()
	{
		return inputEngine;
	}
}

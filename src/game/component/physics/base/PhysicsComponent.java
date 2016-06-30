package game.component.physics.base;

import java.util.ArrayList;

import core.math.vector.Vector2f;
import core.utility.MathUtil;
import game.component.base.GameComponent;
import game.component.physics.BoxCollider;
import game.component.physics.CircleCollider;
import game.component.physics.PolygonCollider;
import game.object.utility.Transform;

public abstract class PhysicsComponent extends GameComponent 
{
	public ArrayList<CollisionData> collisions;
	
	public Transform prevWorld = new Transform();
	public Transform prevLocal = new Transform();
	
	public boolean hasCollided = false;

	public PhysicsComponent(String name) 
	{
		super(name);
		
		collisions = new ArrayList<CollisionData>();
	}
	
	@Override
	public void onAdd()
	{
		game.getPhysicsEngine().register(this);
		updateTransforms();
	}

	@Override
	public void onRemove()
	{
		game.getPhysicsEngine().unregister(this);
	}

	@Override
	public void start() 
	{
	}

	@Override
	public void stop() 
	{
	}
	
	// This function should ultimately handle the collision details for both objects
	public static void checkCollision(PhysicsComponent compA, PhysicsComponent compB) 
	{
		boolean collision = false;
		
		CollisionData dataA = new CollisionData(), dataB = new CollisionData();
		
		dataA.collider = compA;
		dataB.collider = compB;
		
		if (compA.instanceType.id == BoxCollider.type.id)
		{
			// dataA is the box
			if (compB.instanceType.id == BoxCollider.type.id)
				collision = PhysicsComponent.boxVsBox(dataA, dataB);
			if (compB.instanceType.id == CircleCollider.type.id)
				collision = PhysicsComponent.circleVsBox(dataB, dataA);
			if (compB.instanceType.id == PolygonCollider.type.id)
				collision = false;
		}
		else if (compA.instanceType.id == CircleCollider.type.id)
		{
			// dataA is the circle
			if (compB.instanceType.id == BoxCollider.type.id)
				collision = PhysicsComponent.circleVsBox(dataA, dataB);
			if (compB.instanceType.id == CircleCollider.type.id)
				collision = PhysicsComponent.circleVsCircle(dataA, dataB);
			if (compB.instanceType.id == PolygonCollider.type.id)
				collision = false;
		}
		else if (compA.instanceType.id == PolygonCollider.type.id)
		{
			collision = false;
		}
		else
		{
			System.out.println("Error: physics component not recognized: " + compA.instanceType.id + " | " + compB.instanceType.name);
		}
		
		compA.handleCollision(dataB, collision);
		compB.handleCollision(dataA, collision);
	}
	
	public static boolean circleVsCircle(CollisionData circleA, CollisionData circleB)
	{
		CircleCollider a = (CircleCollider)circleA.collider;
		CircleCollider b = (CircleCollider)circleB.collider;
		
		Vector2f p1 = new Vector2f(a.getParent().world.pos.add(a.localOffset));
		Vector2f p2 = new Vector2f(b.getParent().world.pos.add(b.localOffset));
		
		Vector2f v = p2.sub(p1); // this -----> other
		
		float t = a.radius + b.radius;
		boolean collision = v.squaredMagnitude() <= t * t;
		
		if (collision)
		{
			float length = v.magnitude();
			
			if (length != 0)
			{
				circleA.penetration = t - length;
				circleB.penetration = t - length;
				
				v = v.div(length);
			}
			else
			{
				circleA.penetration = b.radius;
				circleB.penetration = a.radius;
				
				v.x = 1;
				v.y = 0;
			}
		
			circleA.normal = new Vector2f(-v.x, -v.y);
			circleB.normal = new Vector2f(v.x, v.y);
		}
		
		return collision;
	}
	
	public static boolean boxVsBox(CollisionData boxA, CollisionData boxB)
	{
		BoxCollider a = (BoxCollider)boxA.collider;
		BoxCollider b = (BoxCollider)boxB.collider;
		
		Vector2f p1 = new Vector2f(a.getParent().world.pos.add(a.localOffset));
		Vector2f p2 = new Vector2f(b.getParent().world.pos.add(b.localOffset));
		
		// Adjust p1 and p2 to be the center
		p1.x += a.halfWidth;
		p1.y += a.halfHeight;
		p2.x += b.halfWidth;
		p2.y += b.halfHeight;
		
		Vector2f v = p2.sub(p1); // a ----> b
		
		float xOverlap = a.halfWidth + b.halfWidth - Math.abs(v.x);
		
		if (xOverlap > 0)
		{
			float yOverlap = a.halfHeight + b.halfHeight - Math.abs(v.y);
			
			if (yOverlap > 0)
			{
				if (xOverlap < yOverlap)
				{
					if (v.x < 0)
					{
						boxA.normal = new Vector2f(1, 0);
						boxB.normal = new Vector2f(-1, 0);
					}
					else
					{
						boxA.normal = new Vector2f(-1, 0);
						boxB.normal = new Vector2f(1, 0);
					}
					
					boxA.penetration = xOverlap;
					boxB.penetration = xOverlap;
					
					return true;
				}
				else
				{
					if (v.y < 0)
					{
						boxA.normal = new Vector2f(0, 1);
						boxB.normal = new Vector2f(0, -1);
					}
					else
					{
						boxA.normal = new Vector2f(0, -1);
						boxB.normal = new Vector2f(0, 1);
					}
					
					boxA.penetration = yOverlap;
					boxB.penetration = yOverlap;

					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean circleVsBox(CollisionData circle, CollisionData box)
	{
		CircleCollider a = (CircleCollider)circle.collider;
		BoxCollider b = (BoxCollider)box.collider;
		
		Vector2f p1 = new Vector2f(b.getParent().world.pos.add(b.localOffset));
		Vector2f p2 = new Vector2f(a.getParent().world.pos.add(a.localOffset));	
		
		// Adjust p1 to be the center of the box
		p1.x += b.halfWidth;
		p1.y += b.halfHeight;
		
		Vector2f v = p2.sub(p1); // b ----> a
		Vector2f closest = new Vector2f(v);
		
		closest.x = MathUtil.clamp(-b.halfWidth, b.halfWidth, closest.x);
		closest.y = MathUtil.clamp(-b.halfHeight, b.halfHeight, closest.y);
		
		boolean circleIsInside = false;
		
		if (v.x == closest.x && v.y == closest.y)
		{
			circleIsInside = true;
			
			if (Math.abs(v.x) > Math.abs(v.y))
			{
				if (closest.x > 0)
					closest.x = b.halfWidth;
				else
					closest.x = -b.halfWidth;
			}
			else
			{
				if (closest.y > 0)
					closest.y = b.halfHeight;
				else
					closest.y = -b.halfHeight;
			}
		}
		
		Vector2f normal = v.sub(closest);
		float dist = normal.squaredMagnitude();
		float radius = a.radius;
		
		if ((dist > radius * radius) && !circleIsInside)
			return false;
		
		dist = (float)Math.sqrt(dist);
		normal = normal.div(dist);
		
		if (!circleIsInside)
		{
			circle.normal = new Vector2f(normal.x, normal.y);
			box.normal = new Vector2f(-normal.x, -normal.y);
		}
		else
		{
			circle.normal = new Vector2f(-normal.x, -normal.y);
			box.normal = new Vector2f(normal.x, normal.y);
		}
		
		circle.penetration = radius - dist;
		box.penetration = radius - dist;
		
		return true;
	}
	
	public void handleCollision(CollisionData data, boolean hasCollided)
	{
		if (hasCollided)
		{
			CollisionData t = null;
			
			for (int i = 0; i < collisions.size(); i++)
			{	
				if (collisions.get(i).collider == data.collider)
				{
					t = collisions.get(i);
					break;
				}
			}
			
			// Only add if it doesn't already exist
			if (t == null)
			{
				data.justCollided = true;
				collisions.add(data);
			}
			else
			{
				t.justCollided = false;
			}
		}
		else
		{
			CollisionData t = null;
			
			for (int i = 0; i < collisions.size(); i++)
			{
				if (collisions.get(i).collider.equals(data.collider))
					t = collisions.get(i);
			}
			
			// Only remove if it exists
			if (t != null)
				collisions.remove(t);
		}
		
		updateTransforms();
	}
	
	void updateTransforms()
	{
		prevLocal.pos = parent.local.pos;
		prevWorld.pos = parent.world.pos;
	}
}

package game.component.physics.base;

import core.math.vector.Vector2f;

public class CollisionData 
{
	public PhysicsComponent collider = null;
	public float penetration = 0;
	public Vector2f normal = null;
	public boolean justCollided = false;
}

package builders;
 
 
 
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
 
import constants.Const;
import constants.ObjectType;

import objects.Enemy;
  
  
  
public class Builder_Enemy
{ 
  private static List<Integer> fields;  
  
  static
  {   
    fields = new ArrayList<>(Arrays.asList(new Integer[] {
      ObjectType.ENEMY.ordinal(),
			0, 0, 0,
      Const.PAYLOAD_OBJECT_END
    }));
  }   
  
  public static int size() { return fields.size(); }
  public static List<Integer> build(Enemy enemy)
  {
    fields.set(1, enemy.x());
    fields.set(2, enemy.y());
		fields.set(3, enemy.lifes());

    return fields;
  }   
}

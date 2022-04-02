package builders;
 
 
 
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
 
import constants.Const;
import constants.ObjectType;

import objects.Player;
  
  
  
public class Builder_Player
{ 
  private static List<Integer> fields;  
  
  static
  {   
    fields = new ArrayList<>(Arrays.asList(new Integer[] {
      ObjectType.PLAYER.ordinal(),
			0, 0, 0, 0, 0, 0,
      Const.PAYLOAD_OBJECT_END
    }));
  }   
  
  public static int size() { return fields.size(); }
  public static List<Integer> build(Player player)
  {
    fields.set(1, player.x());
    fields.set(2, player.y());
    fields.set(3, player.direction());
    fields.set(4, player.id());
    fields.set(5, !player.dead() ? 1 : 0);
		fields.set(6, player.cartridges());

    return fields;
  }   
}

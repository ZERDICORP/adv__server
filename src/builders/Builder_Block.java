package builders;
 
 
 
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
 
import constants.Const;
import constants.ObjectType;

import objects.Block;
  
  
  
public class Builder_Block
{ 
  private static List<Integer> fields;  
  
  static
  {   
    fields = new ArrayList<>(Arrays.asList(new Integer[] {
      ObjectType.BLOCK.ordinal(),
			0, 0, 0,
      Const.PAYLOAD_OBJECT_END
    }));
  }   
  
  public static int size() { return fields.size(); }
  public static List<Integer> build(Block block)
  {
    fields.set(1, block.x());
    fields.set(2, block.y());
		fields.set(3, block.strength());

    return fields;
  }   
}

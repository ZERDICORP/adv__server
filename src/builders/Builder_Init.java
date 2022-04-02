package builders;
 
 
 
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
 
import constants.Const;
import constants.ObjectType;
  
  
  
public class Builder_Init
{ 
  private static List<Integer> fields;  
  
  static
  {   
    fields = new ArrayList<>(Arrays.asList(new Integer[] {
      ObjectType.INIT.ordinal(),
			0, 0, 0, 0, 0,
      Const.PAYLOAD_OBJECT_END
    }));
  }   
  
  public static int size() { return fields.size(); }
  public static List<Integer> build(int id)
  {
    fields.set(1, id);
    fields.set(2, Const.MAP_H);
		fields.set(3, Const.MAP_H);
		fields.set(4, Const.LIVING_SECTOR_W);
		fields.set(5, Const.LIVING_SECTOR_H);

    return fields;
  }   
}

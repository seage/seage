package org.seage.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DataNodeTest {
  @Test
  void testDataNodeValues() throws Exception {
    DataNode dn1 = new DataNode("dn1");
    dn1.putValue("a1", 1);
    dn1.putValue("a2", 1.3);
    dn1.putValue("a3", true);
    dn1.putValue("a4", "text");

    assertEquals(1, dn1.getValueInt("a1"));
    assertEquals(1.3, dn1.getValueDouble("a2"));
    assertEquals(true, dn1.getValueBool("a3"));
    assertEquals("text", dn1.getValueStr("a4"));
  }

  @Test
  void testDataNodeCopyConstructorSimple() throws Exception {
    DataNode dn1 = new DataNode("dn1");
    dn1.putValue("a1", 1);
    dn1.putValue("a2", 1.3);

    DataNode dn2 = new DataNode(dn1);
    assertEquals(1, dn2.getValueInt("a1"));
    assertEquals(1.3, dn2.getValueDouble("a2"));

    dn1.putValue("a1", 2);
    dn1.putValue("a2", 2.3);

    assertEquals(1, dn2.getValueInt("a1"));
    assertEquals(1.3, dn2.getValueDouble("a2"));
  }

  @Test
  void testDataNodeCopyConstructorStructure() throws Exception {
    DataNode dn1 = new DataNode("dn1");
    DataNode dn11 = new DataNode("dn11");
    dn11.putValue("a1", 1);
    dn1.putDataNodeRef(dn11);

    DataNode dn2 = new DataNode(dn1);
    DataNode dn22 = dn2.getDataNode("dn11");

    assertEquals(1, dn22.getValueInt("a1"));

    dn11.putValue("a1", 2);
    assertEquals(2, dn11.getValueInt("a1"));
    assertEquals(1, dn22.getValueInt("a1"));

    dn22.putValue("a1", 3);
    assertEquals(2, dn11.getValueInt("a1"));
    assertEquals(3, dn22.getValueInt("a1"));
  }

  @Test
  void testDataNodeCopyConstructorMoreStructure() throws Exception {
    DataNode dn1 = new DataNode("dn1");
    DataNode dn11 = new DataNode("dn11");
    DataNode dn111 = new DataNode("dn111");
    
    dn111.putValue("a1", 1);
    dn11.putDataNode(dn111);
    dn1.putDataNode(dn11);    

    DataNode dn2 = new DataNode(dn1);
    DataNode dn222 = dn2.getDataNode("dn11").getDataNode("dn111");

    assertEquals(1, dn222.getValueInt("a1"));

    dn111.putValue("a1", 2);
    assertEquals(2, dn111.getValueInt("a1"));
    assertEquals(1, dn222.getValueInt("a1"));

    dn222.putValue("a1", 3);
    assertEquals(2, dn111.getValueInt("a1"));
    assertEquals(3, dn222.getValueInt("a1"));
  }
}

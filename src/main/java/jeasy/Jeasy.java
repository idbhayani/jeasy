package jeasy;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.variant.Variant;

public class Jeasy {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("---------");
		Jeasy test = new Jeasy();
	    
	    JOpc.coInitialize();
	    
	//    JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation.1", "JOPC1");
	    // Test against Prosys simulator
	    JOpc jopc = new JOpc("localhost", "Prosys.OPC.Simulation", "JOPC1");

	//    OpcItem item1 = new OpcItem("T101.F101", true, "");
	//    OpcItem item2 = new OpcItem("T101.L101", true, "");
	    OpcItem item1 = new OpcItem("Random.PsFloat1", true, "");
	    OpcItem item2 = new OpcItem("Random.PsBool1", true, "");

	    OpcGroup group = new OpcGroup("T101", true, 500, 0.0f);
	    
	    group.addItem(item1);
	    group.addItem(item2);

	    jopc.addGroup(group);
	    
	    try {
	      jopc.connect();
	      System.out.println("JOPC client is connected...");
	    }
	    catch (ConnectivityException e2) {
	      e2.printStackTrace();
	    }
	    
	    try {
	      jopc.registerGroups();
	      System.out.println("OPCGroup are registered...");
	    }
	    catch (UnableAddGroupException e2) {
	      e2.printStackTrace();
	    }
	    catch (UnableAddItemException e2) {
	      e2.printStackTrace();
	    }
	    
	    synchronized(test) {
	      test.wait(50);
	    }
	    
	    // Synchronous reading of item
	    int cycles = 7;
	    int acycle = 0;
	    while (acycle++ < cycles) {
	      synchronized(test) {
	        test.wait(1000);
	      }
	      
	      try {
	        OpcItem responseItem = jopc.synchReadItem(group, item1);
	        System.out.println(responseItem);
	      //  System.out.println(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
	      }
	      catch (ComponentNotFoundException e1) {
	        e1.printStackTrace();
	      }
	      catch (SynchReadException e) {
	        e.printStackTrace();
	      }
	    }
	    
	    JOpc.coUninitialize();
	}

}

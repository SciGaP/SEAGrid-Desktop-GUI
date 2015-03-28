package Nanocad3d.xyplotgui.utils;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ComponentUtil {
	
//	public static HashMap ComponentMap;
	public Component ComponentType;
	
	
	
//	public static HashMap createComponentMap(Component ParentComponent) {
//        HashMap ComponentMap = new HashMap<String,Component>();
////        ComponentMap.put(ParentComponent.getName(), ParentComponent);
//        Component[] components;        
//        if(ParentComponent instanceof JFrame){
//        	components = ((JFrame) ParentComponent).getContentPane().getComponents();
//            for (int i=0; i < components.length; i++) {
//                ComponentMap.put(components[i].getName(), components[i]);
//        }
//        }else if (ParentComponent instanceof JPanel){
//        	components = ((JPanel) ParentComponent).getComponents();
//            for (int i=0; i < components.length; i++) {
//                ComponentMap.put(components[i].getName(), components[i]);
//        }
//        }
//        return ComponentMap;
//	}

	public static HashMap createComponentMap(Container ParentComponent) { // recursive add all sub-coponents into hashmap
        HashMap ComponentMap = new HashMap<String,Component>();
        Component[] components = ParentComponent.getComponents(); 
        for (Component comp : components) {
        	if (comp instanceof Container) {
        		ComponentMap.putAll(createComponentMap((Container) comp));
        	}
        	for (int i=0; i < components.length; i++) {
        		ComponentMap.put(components[i].getName(), components[i]);
        	}
        }
               
        return ComponentMap;       
	
	}
	
	
//	public static HashMap AppendComponentMap(Component ParentComponent,HashMap ExistingComponentHashMap) {
//		
//        HashMap ComponentMap = new HashMap<String,Component>();
////        ComponentMap.put(ParentComponent.getName(), ParentComponent);
//        Component[] components;
//        if(ParentComponent instanceof JFrame){
//        	components = ((JFrame) ParentComponent).getContentPane().getComponents();
//            for (int i=0; i < components.length; i++) {
//                ComponentMap.put(components[i].getName(), components[i]);
//        }
//        }else if (ParentComponent instanceof JPanel){
//        	components = ((JPanel) ParentComponent).getComponents();
//            for (int i=0; i < components.length; i++) {
//                ComponentMap.put(components[i].getName(), components[i]);
//        }
//        }
//        ExistingComponentHashMap.putAll(ComponentMap);
//        return ExistingComponentHashMap;
//	}
	
    public static Component getComponentByName(HashMap ComponentMap ,String name) {
            if (ComponentMap.containsKey(name)) {
                    return (Component) ComponentMap.get(name);
            }
            else return null;
    }
    
    
}

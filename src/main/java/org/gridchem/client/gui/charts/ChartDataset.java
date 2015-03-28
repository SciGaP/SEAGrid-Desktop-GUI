/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Feb 15, 2007
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.gui.charts;

import java.util.HashSet;

import org.gridchem.client.exceptions.ChartException;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.ProjectBean;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;

/**
 * Handles the creation of a dataset appropriate for the given ChartType. 
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class ChartDataset {

    
    public enum ChartType {
        SUMMARY("SUMMARY","summary"),
        PIE("PIE","pie"),
        METER("METER","meter"),
        BAR("BAR","bar"),
        LAYERED("LAYERED","layered"),
        STACKED("STACKED","stacked");
        
        String value;
        
        private ChartType(String name, String value) {
            this.value = value;
        }
        
        public String value() {
            return value;
        }
        
        public ChartType next() {
            ChartType[] values = this.values();
            int i;
            
            for(i=0;i<values.length;i++) {
                if (value.equals(values[i].value())) {
                    if (i == values.length - 1) {
                        i=0;
                        break;
                    } else { 
                        i++;
                        break;
                    }
                }
            }
            
            return values[i];
        }
        
        public ChartType previous() {
            ChartType[] values = this.values();
            int i;
            
            for(i=0;i<values.length;i++) {
                if (value.equals(values[i].value())) {
                    if (i == 0) {
                        i = values.length -1 ;
                        break;
                    } else { 
                        i--;
                        break;
                    }
                }
            }
            
            return values[i];
        }
    }
    
    public enum LoadType {
        CPU("CPU","cpu"),
        //MEMORY("MEMORY","memory"),
        QUEUE("QUEUE","queue"),
        DISK("DISK","disk"),
        SUMMARY("SUMMARY","summary");
        
        String value;
        
        private LoadType(String name, String value) {
            this.value = value;
        }
        
        public String value() {
            return value;
        }
    }
    
    private static AbstractDataset dataset;
    
    public static AbstractDataset createDataset(HashSet<ComputeBean> resources, ChartType chartType) {

        if(chartType.equals(ChartType.SUMMARY)) {
            dataset = createBarDataset(resources,LoadType.SUMMARY);

        } else if(chartType.equals(ChartType.BAR)) {
            dataset = createBarDataset(resources,LoadType.SUMMARY);

        } else if(chartType.equals(ChartType.PIE)) {
            dataset = createPieDataset(resources,LoadType.SUMMARY);

        } else if(chartType.equals(ChartType.LAYERED)) {
            dataset = createBarDataset(resources,LoadType.SUMMARY);

        } else if(chartType.equals(ChartType.STACKED)) {
            dataset = createBarDataset(resources,LoadType.SUMMARY);
        } else {
            throw new ChartException("Unknown chart type: " + chartType);
        }
        
        return dataset;
    }
    
    public static AbstractDataset createDataset(HashSet<ComputeBean> resources, 
            ChartType chartType, LoadType loadType) {

        if(chartType.equals(ChartType.SUMMARY)) {
            dataset = createBarDataset(resources,loadType);

        } else if(chartType.equals(ChartType.BAR)) {
            dataset = createBarDataset(resources,loadType);

        } else if(chartType.equals(ChartType.LAYERED)) {
            dataset = createBarDataset(resources,loadType);

        } else if(chartType.equals(ChartType.STACKED)) {
            dataset = createBarDataset(resources,loadType);
            
        } else if(chartType.equals(ChartType.PIE)) {
            dataset = createPieDataset(resources,loadType);
            
        } else if(chartType.equals(ChartType.METER)) {
            dataset = createMeterDataset(resources,loadType);
            
        } else {
            throw new ChartException("Unknown chart type: " + chartType);
        }
        
        return dataset;
    }
    
//    public static AbstractDataset createDataset(ComputeBean project, ChartType chartType, LoadType loadType) {
//        
//        if(chartType.equals(ChartType.SUMMARY)) {
//            dataset = createBarDataset(project.getResources());
//
//        } else if(chartType.equals(ChartType.BAR)) {
//            dataset = createBarDataset(vo.getUser().getCurrentProject().getResources());
//
//        } else if(chartType.equals(ChartType.LAYERED)) {
//            dataset = createBarDataset(vo.getUser().getCurrentProject().getResources());
//
//        } else if(chartType.equals(ChartType.STACKED)) {
//            dataset = createBarDataset(vo.getUser().getCurrentProject().getResources());
//        } else {
//            throw new ChartException("Unknown chart type: " + chartType);
//        }
//        
//        return dataset;
//    }
    
    public static AbstractDataset createDataset(ComputeBean hpc, ChartType chartType) {

        if(chartType.equals(ChartType.SUMMARY)) {
            dataset = createBarDataset(hpc);

        } else if(chartType.equals(ChartType.PIE)) {
            dataset = createPieDataset(hpc,LoadType.CPU);

        } else if(chartType.equals(ChartType.METER)) {
            dataset = createMeterDataset(hpc,LoadType.CPU);

        } else if(chartType.equals(ChartType.BAR)) {
            dataset = createBarDataset(hpc);

        } else if(chartType.equals(ChartType.LAYERED)) {
            dataset = createBarDataset(hpc);

        } else if(chartType.equals(ChartType.STACKED)) {
            dataset = createBarDataset(hpc);
        } else {
            throw new ChartException("Unknown chart type: " + chartType);
        }
        
        return dataset;
    }
    
    public static AbstractDataset createDataset(ComputeBean hpc, ChartType chartType, LoadType loadType) {
        
        if(chartType.equals(ChartType.SUMMARY)) {
            dataset = createBarDataset(hpc);

        } else if(chartType.equals(ChartType.PIE)) {
            dataset = createPieDataset(hpc,loadType);

        } else if(chartType.equals(ChartType.METER)) {
            dataset = createMeterDataset(hpc,loadType);

        } else if(chartType.equals(ChartType.BAR)) {
            dataset = createBarDataset(hpc);

        } else if(chartType.equals(ChartType.LAYERED)) {
            dataset = createBarDataset(hpc);

        } else if(chartType.equals(ChartType.STACKED)) {
            dataset = createBarDataset(hpc);
        } else {
            throw new ChartException("Unknown chart type: " + chartType);
        }
        
        return dataset;
    }
    
//    private static DefaultPieDataset createPieDataset(VO vo, LoadType loadType) {
//        DefaultPieDataset pds = new DefaultPieDataset();
//        
//        if (loadType.equals(LoadType.DISK)) {
//            // disk dataset
//            
//            if (vo.getUser().getCurrentProject().getResources().size() == 1) {
//                ComputeBean hpc = vo.getUser().getCurrentProject().getResources().iterator().next();
//                pds.setValue("Used", hpc.getLoad().getDisk());
//                pds.setValue("Unused", 100 - hpc.getLoad().getDisk());
//            } else {
//                for (ComputeBean hpc: vo.getUser().getCurrentProject().getResources()) {
//                    pds.setValue(hpc.getName(),hpc.getLoad().getDisk());
//                }
//            }
//        } else if (loadType.equals(LoadType.QUEUE)) {
//            // queue dataset
//            if (vo.getUser().getCurrentProject().getResources().size() == 1) {
//                ComputeBean hpc = vo.getUser().getCurrentProject().getResources().iterator().next();
//                pds.setValue("Used", hpc.getLoad().getQueue());
//                pds.setValue("Unused", 100 - hpc.getLoad().getQueue());
//            } else {
//                for (ComputeBean resource: vo.getUser().getCurrentProject().getResources()) {
//                    pds.setValue(resource.getName(),resource.getLoad().getQueue());
//                }
//            }
//        } else {
//            // CPU dataset
//            if (vo.getUser().getCurrentProject().getResources().size() == 1) {
//                ComputeBean hpc = vo.getUser().getCurrentProject().getResources().iterator().next();
//                pds.setValue("Used", hpc.getLoad().getCpu());
//                pds.setValue("Unused", 100 - hpc.getLoad().getCpu());
//            } else {
//                for (ComputeBean resource: vo.getUser().getCurrentProject().getResources()) {
//                    pds.setValue(resource.getName(),resource.getLoad().getCpu());
//                }
//            }
//        }
//        
//        return pds;
//    }
    
    private static DefaultPieDataset createPieDataset(ComputeBean hpc, LoadType loadType) {
        DefaultPieDataset pds = new DefaultPieDataset();
        
        if (loadType.equals(LoadType.DISK)) {
            // disk dataset
            pds.setValue("Used", hpc.getLoad().getDisk());
            pds.setValue("Unused", 100 - hpc.getLoad().getDisk());
        } else if (loadType.equals(LoadType.QUEUE)) {
            // queue dataset
            pds.setValue("Used", hpc.getLoad().getQueue());
            pds.setValue("Unused", 100 - hpc.getLoad().getQueue());
        } else {
            // CPU dataset
            pds.setValue("Used", hpc.getLoad().getCpu());
            pds.setValue("Unused", 100 - hpc.getLoad().getCpu());
        }
        
        return pds;
    }
    
    private static DefaultPieDataset createPieDataset(HashSet<ComputeBean> resources, LoadType loadType) {
        DefaultPieDataset pds = new DefaultPieDataset();
        
        if (loadType.equals(LoadType.DISK)) {
            // disk dataset
            
            if (resources.size() == 1) {
                ComputeBean hpc = resources.iterator().next();
                pds.setValue("Used", hpc.getLoad().getDisk());
                pds.setValue("Unused", 100 - hpc.getLoad().getDisk());
            } else {
                for (ComputeBean hpc: resources) {
                    pds.setValue(hpc.getName(),hpc.getLoad().getDisk());
                }
            }
        } else if (loadType.equals(LoadType.QUEUE)) {
            // queue dataset
            if (resources.size() == 1) {
                ComputeBean hpc = resources.iterator().next();
                pds.setValue("Used", hpc.getLoad().getQueue());
                pds.setValue("Unused", 100 - hpc.getLoad().getQueue());
            } else {
                for (ComputeBean resource: resources) {
                    pds.setValue(resource.getName(),resource.getLoad().getQueue());
                }
            }
        } else {
            // CPU dataset
            if (resources.size() == 1) {
                ComputeBean hpc = resources.iterator().next();
                pds.setValue("Used", hpc.getLoad().getCpu());
                pds.setValue("Unused", 100 - hpc.getLoad().getCpu());
            } else {
                for (ComputeBean resource: resources) {
                    pds.setValue(resource.getName(),resource.getLoad().getCpu());
                }
            }
        }
        return pds;
    }
    
//    /**
//     * Creates 4 value datasets
//     * @return
//     */
//    private static DefaultValueDataset createMeterDataset(VO vo, LoadType loadType) {
//        DefaultValueDataset vds = new DefaultValueDataset();
//        
//        if (loadType.equals(LoadType.DISK)) {
//            // disk dataset
//            for (ComputeBean resource: vo.getUser().getCurrentProject().getResources()) {
//                vds.setValue(resource.getLoad().getDisk());
//            }
////        } else if (loadType.equals(LoadType.MEMORY) ){
////            // memory dataset
////            for (ComputeBean resource: vo.getUser().getCurrentProject().getResources()) {
////                vds.setValue(resource.getLoad().getMemory());
////            }
//        } else if (loadType.equals(LoadType.QUEUE)) {
//            // queue dataset
//            for (ComputeBean resource: vo.getUser().getCurrentProject().getResources()) {
//                vds.setValue(resource.getLoad().getQueue());
//            }
//        } else {
//            // CPU dataset
//            for (ComputeBean resource: vo.getUser().getCurrentProject().getResources()) {
//                vds.setValue(resource.getLoad().getCpu());
//            }
//        }
//        
//        return vds;
//    }
    
    /**
     * Creates a value dataset for the meter display
     * @return
     */
    private static DefaultValueDataset createMeterDataset(ComputeBean hpc, LoadType loadType) {
        DefaultValueDataset vds = new DefaultValueDataset();
        
        if (loadType.equals(LoadType.DISK)) {
            // disk dataset
            vds.setValue(hpc.getLoad().getDisk());
            
//        } else if (loadType.equals(LoadType.MEMORY) ){
//            // memory dataset
//            vds.setValue(hpc.getLoad().getMemory());
        } else if (loadType.equals(LoadType.QUEUE)) {
            // queue dataset
            vds.setValue(hpc.getLoad().getQueue());
        } else {
            // CPU dataset
            vds.setValue(hpc.getLoad().getCpu());
        }
        
        return vds;
    }
    
    private static DefaultValueDataset createMeterDataset(HashSet<ComputeBean> resources, LoadType loadType) {
        DefaultValueDataset vds = new DefaultValueDataset();
        
        for(ComputeBean resource: resources) {
            if (loadType.equals(LoadType.CPU)) {
                vds.setValue(resource.getLoad().getCpu());
            } else if (loadType.equals(LoadType.DISK)) {
                vds.setValue(resource.getLoad().getDisk());
            } else if (loadType.equals(LoadType.QUEUE)) {
                vds.setValue(resource.getLoad().getQueue());
            } else {
                vds.setValue(resource.getLoad().getCpu());
                vds.setValue(resource.getLoad().getDisk());
                vds.setValue(resource.getLoad().getQueue());
            }
        }
        
        return vds;
    }
    
    private static DefaultCategoryDataset createBarDataset(HashSet<ComputeBean> resources) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        
        for(ComputeBean resource: resources) {
            ds.addValue(resource.getLoad().getCpu(), resource.getName(), "CPU");
            ds.addValue(resource.getLoad().getDisk(), resource.getName(), "Disk");
            //ds.addValue(resource.getLoad().getMemory(), resource.getName(), "Memory");
            ds.addValue(resource.getLoad().getQueue(), resource.getName(), "Queue");
            
        }
        
        return ds;
    }
    
    private static DefaultCategoryDataset createBarDataset(HashSet<ComputeBean> resources, LoadType loadType) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        
        for(ComputeBean resource: resources) {
            if (loadType.equals(LoadType.CPU)) {
                ds.addValue(resource.getLoad().getCpu(), resource.getName(), "CPU");
            } else if (loadType.equals(LoadType.DISK)) {
                ds.addValue(resource.getLoad().getDisk(), resource.getName(), "Disk");
            //ds.addValue(resource.getLoad().getMemory(), resource.getName(), "Memory");
            } else if (loadType.equals(LoadType.QUEUE)) {
                ds.addValue(resource.getLoad().getQueue(), resource.getName(), "Queue");
            } else {
                ds.addValue(resource.getLoad().getCpu(), resource.getName(), "CPU");
                ds.addValue(resource.getLoad().getDisk(), resource.getName(), "Disk");
                ds.addValue(resource.getLoad().getQueue(), resource.getName(), "Queue");
            }
            
        }
        
        return ds;
    }
    
    private static DefaultCategoryDataset createBarDataset(ComputeBean hpc) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        
        
        ds.addValue(hpc.getLoad().getCpu(), hpc.getName(), "CPU");
        ds.addValue(hpc.getLoad().getDisk(), hpc.getName(), "Disk");
        //ds.addValue(hpc.getLoad().getMemory(), hpc.getName(), "Memory");
        ds.addValue(hpc.getLoad().getQueue(), hpc.getName(), "Queue");
        
        return ds;
    }
}

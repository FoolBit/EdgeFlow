/*
 * MATLAB Compiler: 7.0 (R2018b)
 * Date: Thu Oct 31 19:58:08 2019
 * Arguments: 
 * "-B""macro_default""-W""java:MATLABOptimizer,MyOptimizer""-T""link:lib""-d""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/jar/MATLABOptimizer/for_testing""class{MyOptimizer:/home/fool/Workspace/EdgeFlow/OptimizerMatlab/Optimizer.m}""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/BlockingOptimizerAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerCC/BlockingOptimizerCC.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/BlockingOptimizerED.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/ComputeBeta.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/ComputeDivisionPercentageED.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/ComputeDivisionPercentageEquivalentAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/ComputeTransmitSpeedAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/ComputeTransmitSpeedED.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/GetAlphaAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerCC/GetAlphaCC.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/GetAlphaED.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/NonBlockingOptimizer/LatencyFunction.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/NonBlockingOptimizer/NonBlockingOptimizeFunction.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/NonBlockingOptimizer/NonBlockingOptimizer.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/OptimizeFunctionAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerCC/OptimizeFunctionCC.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/OptimizeFunctionED.m"
 */

package MATLABOptimizer;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class MATLABOptimizerMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "MATLABOptimi_6C265FBF3E4F5BE7E67C9533FE8EF2D7";
    
    /** Component name */
    private static final String sComponentName = "MATLABOptimizer";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(MATLABOptimizerMCRFactory.class)
        );
    
    
    private MATLABOptimizerMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            MATLABOptimizerMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{9,5,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}

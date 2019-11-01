/*
 * MATLAB Compiler: 7.0 (R2018b)
 * Date: Thu Oct 31 19:58:08 2019
 * Arguments: 
 * "-B""macro_default""-W""java:MATLABOptimizer,MyOptimizer""-T""link:lib""-d""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/jar/MATLABOptimizer/for_testing""class{MyOptimizer:/home/fool/Workspace/EdgeFlow/OptimizerMatlab/Optimizer.m}""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/BlockingOptimizerAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerCC/BlockingOptimizerCC.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/BlockingOptimizerED.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/ComputeBeta.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/ComputeDivisionPercentageED.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/ComputeDivisionPercentageEquivalentAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/ComputeTransmitSpeedAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/ComputeTransmitSpeedED.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/GetAlphaAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerCC/GetAlphaCC.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/GetAlphaED.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/NonBlockingOptimizer/LatencyFunction.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/NonBlockingOptimizer/NonBlockingOptimizeFunction.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/NonBlockingOptimizer/NonBlockingOptimizer.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerAP/OptimizeFunctionAP.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerCC/OptimizeFunctionCC.m""-a""/home/fool/Workspace/EdgeFlow/OptimizerMatlab/BlockingOptimizerED/OptimizeFunctionED.m"
 */

package MATLABOptimizer;

import com.mathworks.toolbox.javabuilder.pooling.Poolable;
import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The <code>MyOptimizerRemote</code> class provides a Java RMI-compliant interface to 
 * MATLAB functions. The interface is compiled from the following files:
 * <pre>
 *  /home/fool/Workspace/EdgeFlow/OptimizerMatlab/Optimizer.m
 * </pre>
 * The {@link #dispose} method <b>must</b> be called on a <code>MyOptimizerRemote</code> 
 * instance when it is no longer needed to ensure that native resources allocated by this 
 * class are properly freed, and the server-side proxy is unexported.  (Failure to call 
 * dispose may result in server-side threads not being properly shut down, which often 
 * appears as a hang.)  
 *
 * This interface is designed to be used together with 
 * <code>com.mathworks.toolbox.javabuilder.remoting.RemoteProxy</code> to automatically 
 * generate RMI server proxy objects for instances of MATLABOptimizer.MyOptimizer.
 */
public interface MyOptimizerRemote extends Poolable
{
    /**
     * Provides the standard interface for calling the <code>Optimizer</code> MATLAB 
     * function with 6 input arguments.  
     *
     * Input arguments to standard interface methods may be passed as sub-classes of 
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of any 
     * supported Java type (i.e. scalars and multidimensional arrays of any numeric, 
     * boolean, or character type, or String). Arguments passed as Java types are 
     * converted to MATLAB arrays according to default conversion rules.
     *
     * All inputs to this method must implement either Serializable (pass-by-value) or 
     * Remote (pass-by-reference) as per the RMI specification.
     *
     * Documentation as provided by the author of the MATLAB function:
     * <pre>
     * %
     * %
     * </pre>
     *
     * @param nargout Number of outputs to return.
     * @param rhs The inputs to the MATLAB function.
     *
     * @return Array of length nargout containing the function outputs. Outputs are 
     * returned as sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>. 
     * Each output array should be freed by calling its <code>dispose()</code> method.
     *
     * @throws java.rmi.RemoteException An error has occurred during the function call or 
     * in communication with the server.
     */
    public Object[] Optimizer(int nargout, Object... rhs) throws RemoteException;
  
    /** 
     * Frees native resources associated with the remote server object 
     * @throws java.rmi.RemoteException An error has occurred during the function call or in communication with the server.
     */
    void dispose() throws RemoteException;
}

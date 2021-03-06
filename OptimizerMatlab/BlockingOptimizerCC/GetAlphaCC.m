function alpha = GetAlphaCC(...
    nAP,...
    compressionRatio,...
    totalComputeResourceCC,...
    totalComputeResourceAP,...
    childStartIdx,...
    nChilds,...
    transmitSpeedAP,...
    transmitSpeedED,....
    generateSpeedED,...
    computeCapacityED...
    )

fun = @(x)OptimizeFunctionCC(...
    x,...
    nAP,...
    compressionRatio,...
    totalComputeResourceCC,...
    totalComputeResourceAP,...
    childStartIdx,...
    nChilds,...
    transmitSpeedAP,...
    transmitSpeedED,....
    generateSpeedED,...
    computeCapacityED...
    );

x0 = rand();
[alpha, fval] = fsolve(fun, x0);

end
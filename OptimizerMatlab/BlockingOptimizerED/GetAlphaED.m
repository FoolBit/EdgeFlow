function alpha = GetAlphaED(transmitSourceAP, compressionRatio, computeCapacity, generateSpeed)

fun = @(x)OptimizeFunctionED(x, transmitSourceAP, compressionRatio, computeCapacity, generateSpeed);

x0 = rand();
[alpha, fval] = fsolve(fun, x0);

end
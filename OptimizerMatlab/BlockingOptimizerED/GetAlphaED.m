function alpha = GetAlphaED(transmitSourceAP, compressionRatio, computeCapacityED, generateSpeedED)

fun = @(x)OptimizeFunctionED(x, transmitSourceAP, compressionRatio, computeCapacityED, generateSpeedED);

x0 = rand();
[alpha, fval] = fsolve(fun, x0);

end
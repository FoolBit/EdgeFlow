function f = OptimizeFunctionED(alpha, transmitSourceAP, compressionRatio, computeCapacity, generateSpeed)
% 联立方程的变形
% 用来优化求解alpha

divisionPercentage = ComputeDivisionPercentageED(alpha, computeCapacity, generateSpeed);
transmitSpeed = ComputeTransmitSpeedED(compressionRatio, divisionPercentage, computeCapacity);

f = abs(sum(transmitSpeed) - transmitSourceAP);
end
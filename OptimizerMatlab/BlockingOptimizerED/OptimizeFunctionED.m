function f = OptimizeFunctionED(alpha, transmitSourceAP, compressionRatio, computeCapacity, generateSpeed)
% �������̵ı���
% �����Ż����alpha

divisionPercentage = ComputeDivisionPercentageED(alpha, computeCapacity, generateSpeed);
transmitSpeed = ComputeTransmitSpeedED(compressionRatio, divisionPercentage, computeCapacity);

f = abs(sum(transmitSpeed) - transmitSourceAP);
end
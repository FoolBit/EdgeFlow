function f = OptimizeFunctionED(alpha, transmitSourceAP, compressionRatio, computeCapacityED, generateSpeedED)
% �������̵ı���
% �����Ż����alpha

divisionPercentage = ComputeDivisionPercentageED(alpha, computeCapacityED, generateSpeedED);
transmitSpeed = ComputeTransmitSpeedED(compressionRatio, divisionPercentage, computeCapacityED);

f = abs(sum(transmitSpeed) - transmitSourceAP);
end
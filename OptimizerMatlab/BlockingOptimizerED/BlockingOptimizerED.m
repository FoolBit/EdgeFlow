function [divisionPercentage, transmitSpeed] = BlockingOptimizerED(transmitSourceAP,compressionRatio, paramsED)
% transmit source AP: total transmit source at AP j
% paramsED: [computeCapacity, generateSpeed]

% ��ȡED����
computeCapacity = paramsED(:, 1);
generateSpeed = paramsED(:, 2);

% �Ż����
alpha = GetAlphaED(transmitSourceAP, compressionRatio, computeCapacity, generateSpeed); % division percentage for ED 1

% �����µ�ED����
divisionPercentage = ComputeDivisionPercentageED(alpha, computeCapacity, generateSpeed);
transmitSpeed = ComputeTransmitSpeedED(compressionRatio, divisionPercentage, computeCapacity);

end


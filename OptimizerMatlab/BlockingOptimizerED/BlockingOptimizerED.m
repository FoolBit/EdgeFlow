function [divisionPercentage, transmitSpeed] = BlockingOptimizerED(transmitSourceAP,compressionRatio, paramsED)
% transmit source AP: total transmit source at AP j
% paramsED: [computeCapacity, generateSpeed]

% 提取ED参数
computeCapacity = paramsED(:, 1);
generateSpeed = paramsED(:, 2);

% 优化求解
alpha = GetAlphaED(transmitSourceAP, compressionRatio, computeCapacity, generateSpeed); % division percentage for ED 1

% 返回新的ED参数
divisionPercentage = ComputeDivisionPercentageED(alpha, computeCapacity, generateSpeed);
transmitSpeed = ComputeTransmitSpeedED(compressionRatio, divisionPercentage, computeCapacity);

end


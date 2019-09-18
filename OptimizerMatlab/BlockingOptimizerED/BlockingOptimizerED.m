function [divisionPercentageED, transmitSpeedED] = BlockingOptimizerED(transmitSourceAP,compressionRatio, generateSpeedED, computeCapacityED)
% transmit source AP: total transmit source at AP j

% �Ż����?
alpha = GetAlphaED(transmitSourceAP, compressionRatio, computeCapacityED, generateSpeedED); % division percentage for ED 1

% �����µ�ED����
divisionPercentageED = ComputeDivisionPercentageED(alpha, computeCapacityED, generateSpeedED);
transmitSpeedED = generateSpeedED .* (1 + (compressionRatio - 1) * divisionPercentageED);

end


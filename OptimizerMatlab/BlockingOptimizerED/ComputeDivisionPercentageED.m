function divisionPercentageED = ComputeDivisionPercentageED(alpha, computeCapacityED, generateSpeedED)

k = alpha * generateSpeedED(1) / computeCapacityED(1);
divisionPercentageED = k * computeCapacityED ./ generateSpeedED;

% if division percentage > 1
index = divisionPercentageED > 1;
divisionPercentageED(index) = 1;

end
function divisionPercentage = ComputeDivisionPercentageED(alpha, computeCapacity, generateSpeed)

k = alpha * generateSpeed(1) / computeCapacity(1);
divisionPercentage = k * computeCapacity ./ generateSpeed;

% if division percentage > 1
index = divisionPercentage > 1;
divisionPercentage(index) = 1;

end
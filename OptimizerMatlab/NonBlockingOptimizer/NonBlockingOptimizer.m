function [divisionPercentageED, transmitSpeedED, computeCapacityAP, divisionPercentageAP, transmitSpeedAP] = ...
    NonBlockingOptimizer(...
    nAP,...
    nED,...
    childStartIdx,...
    nChilds,...
    compressionRatio,...
    totalTransmitResourceCC, ...
    totalComputeResourceCC,...
    totalTransmitResourceAP,...
    totalComputeResourceAP,...
    computeCapacityED,...
    generateSpeedED...
    )


fun = @(x)NonBlockingOptimizeFunction(...
    x,...
    nAP,...
    nED,...
    childStartIdx,...
    nChilds,...
    compressionRatio,...
    totalTransmitResourceCC, ...
    totalComputeResourceCC,...
    totalTransmitResourceAP,...
    totalComputeResourceAP,...
    computeCapacityED,...
    generateSpeedED);

maxIter = 10;
L = Inf;
divisionPercentage = [];
lb = zeros(nED+1, 1);
ub = ones(nED+1, 1);
for i = 1:maxIter
    x = randn(nED+1,1);
    [x, fval] = simulannealbnd(fun,x0,lb,ub);
    if(fval < L)
        L = fval;
        divisionPercentage = x;
    end
end

divisionPercentageAP = divisionPercentage(1:nED);

divisionPercentageEDFirst = divisionPercentage(end);
k = sqrt(generateSpeedED(1)*divisionPercentageEDFirst) / computeCapacityED(1);
divisionPercentageED = (k * computeCapacityED ./ sqrt(generateSpeedED)).^2;

%% compute other params using division percentage
computeCapacityAP = sqrt(generateSpeedED .* divisionPercentageAPOri) / k;

transmitSpeedED = sqrt(...
    generateSpeedED ...
    .* (compressionRatio*divisionPercentageEDOri + divisionPercentageAPOri + divisionPercentageCCOri));
transmitSpeedED = transmitSpeedED / sum(transmitSpeedED) * totalTransmitResourceAP;

transmitSpeedAP = sqrt(generateSpeedED .* ...
    (compressionRatio*(divisionPercentageEDOri + divisionPercentageAPOri) + divisionPercentageCCOri) );
transmitSpeedAP = transmitSpeedAP / sum(transmitSpeedAP) * totalTransmitResourceCC;

%% division percentage above is different
% where s_ED + s_AP + s_CC = 1
% therefore we should adjust
divisionPercentageAP = (1 - divisionPercentageED) .* divisionPercentageAP;

end

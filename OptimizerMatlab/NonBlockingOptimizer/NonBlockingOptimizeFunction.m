function f = NonBlockingOptimizeFunction(...
    divisionPercentage,...
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
    generateSpeedED)
%% init division percentage
divisionPercentageAPOri = divisionPercentage(1:nED);

divisionPercentageEDFirst = divisionPercentage(end);
k = sqrt(generateSpeedED(1)*divisionPercentageEDFirst) / computeCapacityED(1);
divisionPercentageEDOri = (k * computeCapacityED ./ sqrt(generateSpeedED)).^2;

divisionPercentageCCOri = 1 - divisionPercentageAPOri - divisionPercentageEDOri;
%% compute other params using division percentage
computeCapacityAP = sqrt(generateSpeedED .* divisionPercentageAPOri) / k;
computeCapacityCC = sqrt(generateSpeedED .* divisionPercentageCCOri) / k;

transmitSpeedED = sqrt(...
    generateSpeedED ...
    .* (compressionRatio*divisionPercentageEDOri + divisionPercentageAPOri + divisionPercentageCCOri));
transmitSpeedED = transmitSpeedED / sum(transmitSpeedED) * totalTransmitResourceAP;

transmitSpeedAP = sqrt(generateSpeedED .* ...
    (compressionRatio*(divisionPercentageEDOri + divisionPercentageAPOri) + divisionPercentageCCOri) );
transmitSpeedAP = transmitSpeedAP / sum(transmitSpeedAP) * totalTransmitResourceCC;

%% adjust division
divisionPercentageED = divisionPercentageEDOri;
divisionPercentageAP = (1 - divisionPercentageED) .* divisionPercentageAPOri;
%% judge blocking
blocking = 0;
% eq (1)
if(sum(divisionPercentageED > 1)>0)
    blocking = 1;
end

% eq (3)
if(blocking == 0)
    transED = generateSpeedED .* (1 + (compressionRatio-1)*divisionPercentageED);
    if(sum(transED > transmitSpeedED) > 0)
        blocking = 1;
    end
end

% eq (4) ok
% eq (7) ok
% eq (9)
if(blocking == 0)
    for i = 1:nAP
        startIdx = childStartIdx(i);
        endIdx = startIdx + nChilds(i) -1;
        computeAP = sum(computeCapacityAP(startIdx:endIdx));
        if(computeAP > totalComputeResourceAP(i))
            blocking = 1;
            break;
        end
    end
end

% eq (10)
if(blocking == 0)
    generateSpeedAP = transmitSpeedED ...
        .* (1 - divisionPercentageED)...
        ./ (1 + (compressionRatio - 1)*divisionPercentageED);
    betaAP = transmitSpeedED - generateSpeedAP;
    
    transAP = generateSpeedAP ...
        .* (1 + (compressionRatio-1)*divisionPercentageAP)...
        +betaAP;
    if(sum(transAP > transmitSpeedAP) > 0)
        blocking = 1;
    end
end

% eq (11) ok
% eq (13) (14) ignore
% eq (15)
if(blocking == 0)
    generateSpeedCC = ...
        transmitSpeedAP ...
        .* (1 - divisionPercentageAP)...
        ./ (1 + (compressionRatio-1)*divisionPercentageAP + compressionRatio * divisionPercentageED ./ (1 - divisionPercentageED));
    if(sum(generateSpeedCC > computeCapacityCC) > 0)
        blocking = 1;
    end
end

% eq (16)
if(blocking == 0)
    computeCC = sum(computeCapacityCC);
    if(computeCC > totalComputeResourceCC)
        blocking = 1;
    end
end

%%
if(blocking == 1)
    f = Inf;
else
    computeCapacityTotal = ...
        sum(computeCapacityED)...
        + totalComputeResourceAP...
        + totalComputeResourceCC;
    
    transmitResourceWireless = sum(totalTransmitResourceAP);
    transmitResourceWired = totalTransmitResourceCC;
    
    f = LatencyFunction(...
        compressionRatio,...
        generateSpeedED, ...
        divisionPercentageEDOri,...
        divisionPercentageAPOri,...
        divisionPercentageCCOri,...
        computeCapacityTotal,...
        transmitResourceWireless,...
        transmitResourceWired);
end
end


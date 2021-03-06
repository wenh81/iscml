classdef PSK < CreateModulation
    
    methods
        function obj = PSK( Order, MappingType, SignalProb )
            % Calling Syntax: obj = PSK( [Order] [,MappingType/MappingVector] [,SignalProb] )
            % MappingType='gray','SP'(Order=4,8),'SSP'(Order=8), or 'MSEW'(Order=8).
            if( nargin<1 || isempty(Order) )
                Order = 8;
            elseif( rem( log2(Order),1 ) )
                % Making sure that modulation Order is a power of 2.
                error( 'PSK:InvalidOrder', 'The order of modulation MUST be a power of 2.' );
            end
            if( nargin<2 || isempty(MappingType) )
                MappingType = [];
                MappingVector = 0:Order-1;
            elseif ~ischar(MappingType)
                if (length(MappingType) ~= Order)
                    error('PSK:InvMappingVec', 'Length of MappingType must be EQUALL to the Order of modulation.');
                elseif (sum( sort(MappingType) ~= (0:Order-1) ) > 0)
                    error( 'PSK:InvMappingVec', 'MappingType must contain all integers 0 through Order-1.' );
                end
                MappingVector = MappingType;
                MappingType = 'UserDefined';
            end
            if( nargin<3 ), SignalProb = []; end
                        
            Temp = exp( j*2*pi*(0:Order-1)/Order );
            if ( ischar(MappingType) && ~strcmpi(MappingType, 'UserDefined') )
                switch MappingType
                    case 'gray'
                        MappingVector = [0 1];
                        for m = 2:log2(Order)
                            MappingVector = [ MappingVector, 2^(m-1) + fliplr(MappingVector) ];
                        end
                    case 'SP'
                        if (Order == 8)
                            MappingVector = [0 1 2 3 4 5 6 7];
                        elseif (Order == 4)
                            MappingVector = [0 1 2 3];
                        else
                            error('PSK:InvMappingType', 'SP coded PSK is ONLY supported for Order=4 or 8.');
                        end
                    case 'SSP'
                        if Order == 8
                            MappingVector = [0 5 2 7 4 1 6 3];
                        else
                            error( 'PSK:InvMappingType', 'SSP coded PSK is ONLY supported for Order=8.' );
                        end
                    case 'MSEW'
                        if Order == 8
                            MappingVector = [0 3 5 6 1 2 4 7];
                        else
                            error( 'PSK:InvMappingType', 'MSEW coded PSK is ONLY supported for Order=8.' );
                        end
                    otherwise
                        error('PSK:InvMappingType', 'Labeling (MappingType) or symbol set size (Order) is not supported for PSK modulation.');
                end
            end
            
            Constellation( MappingVector+1 ) = Temp;
            SignalSet = [real(Constellation); imag(Constellation)];
            
            obj@CreateModulation(SignalSet, SignalProb);
            obj.Type = 'PSK';
            obj.MappingType = MappingType;
%             obj.MappingVector = MappingVector;
        end
    end
    
end
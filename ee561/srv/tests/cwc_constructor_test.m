% Test the constructor of the cluster worker
%   controller.
%
% Version 1
% 2/27/2011
% Terry Ferrett

function cwc_obj = cwc_constructor_test(cml_home)

cmlRoot = cml_home;

% Create cluster worker object
cwc_obj = cwc(cmlRoot, 'test.cfg', 'stub_worker');

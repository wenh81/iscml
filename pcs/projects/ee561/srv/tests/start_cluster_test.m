% Test the cluster-wide start method.
%
% Version 1
% 2/28/2011
% Terry Ferrett

function cwc_obj = start_cluster_test()

cd ..;
cd ..;
cmlRoot = pwd;
CmlStartup
cd srv/
cd tests/

% Create cluster worker object
cwc_obj = cwc(cmlRoot, 'test.cfg', 'stub_worker');

cwc_obj.cSta();

% Wait 10 seconds, and then slay all workers.
pause(10);

cwc_obj.cSto();

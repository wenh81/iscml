clear

% Navigate to *my* directory from wcrlcluser
cd /home/mvalenti/svn/ahfh

% update your path
LocalStartup; % assuming you are in my root ahfh directory

% the root directory (be sure to run LocalStartup)
ahfhRoot = cml_home;

% You need to create an OutageNakagami object and save it

% set the parameters
m = 3;
M = 50;
m_i = 4;  % should be constant for all interferers (for now)

% Create an object
% First load in the Omegas (or use your network class)
% This one has 20 networks, each of size 50
load( [ahfhRoot '/tables/Omega_N20_M50_r30.mat'] )

% The object needs to be called "b" when you save it
% For now set m_i to a scalar
% You will be using my version of OutageNakagami
b = OutageNakagami( Omega, m, m_i );

% Notice we are no longer saving just Omega
NetworkFileName = 'Network1.mat';

% save b [NOTICE CHMOD STATEMENT]
save( 'TempSave.mat', 'b' );
system( 'chmod 666 TempSave.mat' );
system( ['mv TempSave.mat ' ahfhRoot '/tables/' NetworkFileName ] );

% old syntax, which doesn't set permissions correctly
% save( [ahfhRoot '/tables/' NetworkFileName ], 'b' );

% Now setup your jobs.
SNRdB = [10 20];

% Test multiple jobs
NumberJobs = 10;

% Create an array of JobManager Objects
for job=1:NumberJobs
    
    % Create the Param structure
    JobParam(job).m_i = 3*ones(1,M);
    JobParam(job).m = m;
    JobParam(job).Gamma = 10.^(SNRdB/10);
    JobParam(job).Beta = [0.1 1 10];
    JobParam(job).p = [1 0.1 0.01 0.001];
    JobParam(job).NetworkFileName = NetworkFileName;
    
    JobFileName = ['WCRL' int2str(job) '.mat'];
    
    % Create a JobController object
    % In practice, should have a different JobParam set for each job
    JobObject(job) = JobManager( JobParam(job), JobFileName, ahfhRoot );
    
end

% Now submit the jobs
% This needs to be throttled to 5-seconds per job
% in order to avoid "collisions"
for job=1:NumberJobs    
    % Submit the job
    JobObject(job).SubmitJob( );
    pause( 5 );
    
end

% check on the status
for i=1:length( cwc_obj.nodes )
    command_string = ['ssh ' cwc_obj.nodes{i} ' ps aux|grep -i outage_worker'];
    [~,zz] = system( command_string );
    fprintf( 'Node %s\n', cwc_obj.nodes{i} );
    fprintf( '%s\n', zz );
end





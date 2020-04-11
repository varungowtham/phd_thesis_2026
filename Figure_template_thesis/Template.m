clear
close all
clc

%% LaTeX editor settings
set(groot,'defaultTextInterpreter','tex');                                  % how input is interpreted
set(groot, 'defaultAxesTickLabelInterpreter','tex');
set(groot, 'defaultLegendInterpreter','tex');

%% Create class objects
Figure = figureTemplateClass;
Figure2 = figureTemplateClass;

%% Configure object 1
Figure.dataFileName = 'TemplateData.dat';                                   % file name of the loaded data
Figure.savePath = 'TemplatePathExample';                                    % path in which the figure shall be saved
Figure.figureName = 'Template';                                             % what is the figure's name
Figure.fontName = 'Palatino Linotype';
Figure.axisSpecs = [-2 2 0.5 -4 4 1.0];                                     % axis limits for both axes [xstart xend xstep ystart yend ystep]
Figure.xTickFormat = '%.1f';                                                % how many decimal numbers for x axis
Figure.yTickFormat = '%.2f';                                                % how many decimal numbers for y axis
Figure.xDescription = 'Variable';
Figure.xVariable = 'x';
Figure.xUnit = '-';
Figure.yDescription = 'Variable';
Figure.yVariable = 'y';
Figure.yUnit = '-';
Figure.savePNG = false;                                                     % define the file formats that shall be generated
Figure.savePDF = true;
Figure.saveSVG = true;
if ~exist(Figure.savePath,'dir')                                            % check whether path exists, if not create it!
    mkdir(Figure.savePath)
end

%% Configure object 2
Figure2.dataFileName = 'TemplateData.dat';                                  % file name of the loaded data
Figure2.savePath = 'TemplatePathExample';                                   % path in which the figure shall be saved
Figure2.figureName = 'Template2';                                           % what is the figure's name
Figure2.fontName = 'Palatino Linotype';
Figure2.axisSpecs = [1E-4 1E-3 1E-4 1E-6 1E6 1E5];                          % axis limits for both axes [xstart xend xstep ystart yend ystep]
Figure2.xTickFormat = '%.1f';                                               % how many decimal numbers for x axis
Figure2.yTickFormat = '%.1f';                                               % how many decimal numbers for y axis
Figure2.xDescription = 'Molar volume';
Figure2.xVariable = 'v';
Figure2.xUnit = 'm^3 mol^{-1}';
Figure2.yDescription = 'Pressure';
Figure2.yVariable = 'P';
Figure2.yUnit = 'Pa';
Figure2.savePNG = false;                                                    % define the file formats that shall be generated
Figure2.savePDF = true;
Figure2.saveSVG = true;
if ~exist(Figure2.savePath,'dir')                                           % check whether path exists, if not create it!
    mkdir(savePath)
end

%% Data generated in Matlab for object 1
maximumPower=4;
x=linspace(-2,2,100);
y=zeros(maximumPower+1,length(x));
for i=0:1:maximumPower
    y(i+1,:)=x.^i;
end

%% Data read from .dat file for object 2
fileData = readtable(Figure.dataFileName,'format','%f%f');
xValues = table2array(fileData(:,1));
yValues = table2array(fileData(:,2));

%% Generate plot for object 1
Figure.createPlot()                                                         % creates a plot based on the above specifications
set(groot, 'defaultAxesFontName', Figure.fontName);                         % set font name for all figures
L = cell(1,size(y,1));
for yIndex=1:size(y,1)
    plot(x,y(yIndex,:),'lineWidth',Figure.lineWidth,'lineStyle',Figure.lineStyle{yIndex},'Color',Figure.color(yIndex,:),'marker',Figure.markerStyle{yIndex});
    hold on
    L{yIndex} = sprintf('y_%i',yIndex);
end
legend(L,'location','best')
Figure.setXLabel;
Figure.setYLabel;
Figure.savePlot;

%% Generate plot for object 2
Figure2.createPlot() 
set(groot, 'defaultAxesFontName', Figure2.fontName);                        % set font name for all figures
plot(xValues,yValues,'lineWidth',Figure2.lineWidth,'lineStyle',Figure2.lineStyle{1},'Color',Figure2.color(1,:),'marker',Figure2.markerStyle{1});
Figure2.setXLabel;
Figure2.setYLabel;
Figure2.xAxisLogScale()                                                     % for log x axis
% Figure2.yAxisLogScale()                                                     % for log y axis
Figure2.savePlot;
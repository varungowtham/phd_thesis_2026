classdef figureTemplateClass < handle
    
    properties                                                              % default properties are given
        figureName = 'figureTemplate'; 
        fontName = 'arial';                                                 % font name        
        dataFileName = '';                                                  % file name of the data
        axisSpecs = [0 1 0.1 0 1 0.1];                                      % axis limits for both axes [xstart xend xstep ystart yend ystep]
        xTickFormat = '%.1f';                                               % how many decimal numbers for x axis
        yTickFormat = '%.1f';                                               % how many decimal numbers for y axis
        xDescription = 'Variable';
        xVariable = 'x';
        xUnit = '-';
        yDescription = 'Variable';
        yVariable = 'y';
        yUnit = '-';   
        savePath = 'Figures';
        savePNG = false;
        savePDF = true;
        saveSVG = false;
    end
    
    properties (SetAccess = private, GetAccess=public)
        fontSize = 18 ;                                                     % font size
        lineWidth = 3;                                                      % line width
        lineStyle = {'-','--','-.',':','-'};                                % possible line styles
        markerStyle = {'s','p','v','^','x'};                                % possible markers
        color = [                                                           % possible colors (corporate design TUB)
            197 14 31
            0 0 0
            113 113 113
            243 148 0
            0 121 154    
            0 123 106
            ]/255;
        paperSize = [20 15];                                                % how large is the figure (in cm)        
    end
    
    methods
        function createPlot(self)
            % Figure Name 
            f = figure;
            set(f,'NumberTitle','off','name',self.figureName);

            % Set Font, Options, and Image Size
            set(gca, 'FontName', self.fontName)
            set(gcf, 'Units', 'Centimeters', 'Position', [5 5 self.paperSize], 'PaperUnits', 'Centimeters', 'PaperSize', self.paperSize)
            set(gca,'FontSize',self.fontSize, 'FontUnits','points');

            %% Plot Design
            ax=gca;
            set(gca,'TickDir','out');                                       % The only other option is 'in'
            box on
            grid on
            hold on
            ax.XColor = 'black';
            ax.YColor = 'black';

            xl = self.axisSpecs(1);
            xh = self.axisSpecs(2);
            xi = self.axisSpecs(3);
            yl = self.axisSpecs(4);
            yh = self.axisSpecs(5);
            yi = self.axisSpecs(6);
            xlim([xl xh])
            ylim([yl yh])
            xtickformat(self.xTickFormat)
            ytickformat(self.yTickFormat)
            ax.XAxis.TickValues = xl:xi:xh;
            ax.YAxis.TickValues = yl:yi:yh;  
        end
        
        function setXLabel(self)
            xlabel(append(self.xDescription, ' {\it', self.xVariable, '} in ', self.xUnit))
        end
        
        function setYLabel(self)
            ylabel(append(self.yDescription, ' {\it', self.yVariable, '} in ', self.yUnit))
        end        
        
        function xAxisLogScale(self)
            set(gca,'xscale','log')
            xtick=10.^(log10(self.axisSpecs(1)):log10(self.axisSpecs(2)));
            xticklab = cellstr(num2str(round(log10(xtick(:))), '10^{%d}'));
            set(gca,'XTick',xtick,'XTickLabel',xticklab,'TickLabelInterpreter','tex')
        end 
        
        function yAxisLogScale(self)
            set(gca,'yscale','log')
            ytick=10.^(log10(self.axisSpecs(4)):log10(self.axisSpecs(5)));
            yticklab = cellstr(num2str(round(log10(ytick(:))), '10^{%d}'));
            set(gca,'YTick',ytick,'YTickLabel',yticklab,'TickLabelInterpreter','tex')
        end   
        
        function savePlot(self)
            if self.savePNG
                print(sprintf('%s/%s.png',self.savePath,self.figureName),'-dpng','-r600') 
            end
            if self.savePDF
                print(sprintf('%s/%s.pdf',self.savePath,self.figureName),'-dpdf')   
            end
            if self.saveSVG
                print(sprintf('%s/%s.svg',self.savePath,self.figureName),'-dsvg')
            end
        end
            
    end
    
end
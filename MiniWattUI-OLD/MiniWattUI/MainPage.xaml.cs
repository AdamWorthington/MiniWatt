using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Storage.Pickers;
using Windows.Storage;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=402352&clcid=0x409

namespace MiniWattUI
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        private StorageFile _questionsFile = null;
        private StorageFile _sourceFile = null;

        public MainPage()
        {
            this.InitializeComponent();
        }

        private void Menu_Button_Click(object sender, RoutedEventArgs e)
        {
            Splitter.IsPaneOpen = !Splitter.IsPaneOpen;
        }

        
        private async void QuestionsBrowseButton_Click(object sender, RoutedEventArgs e)
        {
            FileOpenPicker openPicker = new FileOpenPicker();
            openPicker.ViewMode = PickerViewMode.Thumbnail;
            openPicker.SuggestedStartLocation = PickerLocationId.DocumentsLibrary;
            openPicker.FileTypeFilter.Add(".jpg");
            openPicker.FileTypeFilter.Add(".jpeg");
            openPicker.FileTypeFilter.Add(".pdf");
            _questionsFile = await openPicker.PickSingleFileAsync();
            if (_questionsFile != null)
            {
                QuestionFileChosenText.Visibility = Visibility.Visible;
                QuestionFileChosenText.Text = "File Chosen: " + _questionsFile.Name;
                QuestionsClearButton.Visibility = Visibility.Visible;
            }
            else
            {
                QuestionFileChosenText.Visibility = Visibility.Collapsed;
                QuestionFileChosenText.Text = "File Chosen: ";
            }
        }

        private void QuestionsClearButton_Click(object sender, RoutedEventArgs e)
        {
            _questionsFile = null;
            QuestionsClearButton.Visibility = Visibility.Collapsed;
            QuestionFileChosenText.Visibility = Visibility.Collapsed;
        }

        private async void SourceBrowseButton_Click(object sender, RoutedEventArgs e)
        {
            FileOpenPicker openPicker = new FileOpenPicker();
            openPicker.ViewMode = PickerViewMode.Thumbnail;
            openPicker.SuggestedStartLocation = PickerLocationId.DocumentsLibrary;
            openPicker.FileTypeFilter.Add(".jpg");
            openPicker.FileTypeFilter.Add(".jpeg");
            openPicker.FileTypeFilter.Add(".pdf");
            _sourceFile = await openPicker.PickSingleFileAsync();
            if (_questionsFile != null)
            {
                SourceFileChosenText.Visibility = Visibility.Visible;
                SourceFileChosenText.Text = "File Chosen: " + _questionsFile.Name;
                SourceClearButton.Visibility = Visibility.Visible;
            }
            else
            {
                SourceFileChosenText.Visibility = Visibility.Collapsed;
                SourceFileChosenText.Text = "File Chosen: ";
            }
        }

        private void SourceClearButton_Click(object sender, RoutedEventArgs e)
        {
            _sourceFile = null;
            SourceClearButton.Visibility = Visibility.Collapsed;
            SourceFileChosenText.Visibility = Visibility.Collapsed;
        }

        private void SubmitButton_Click(object sender, RoutedEventArgs e)
        {

        }
    }
}

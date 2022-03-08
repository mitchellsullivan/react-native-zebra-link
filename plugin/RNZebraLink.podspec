require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "RNZebraLink"
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']

  s.authors      = package['author']
  s.homepage     = package['homepage']
  s.summary      = package['summary']

  s.platforms    = { :ios => "9.0", :tvos => "9.2", :osx => "10.14" }

#   s.source       = { :git => "https://github.com/mitchellsullivan/react-native-zebra-link.git", :tag => "v#{s.version}" }
#   s.source_files  = "ios/**/*.{h,m}"

  s.source       = { :http => 'file:' + __dir__ + '/' }
  s.source_files = "ios/**/*.{h,m}"

  s.dependency 'React-Core'
end

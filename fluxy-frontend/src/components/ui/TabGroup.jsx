export default function TabGroup({ tabs, activeTab, onTabChange, className = "" }) {
  return (
    <div className={`flex gap-6 ${className}`}>
      {tabs.map((tab) => (
        <button
          key={tab.value}
          onClick={() => onTabChange(tab.value)}
          className={`pb-2 text-sm font-medium transition-colors cursor-pointer !bg-transparent !text-sm !p-0 !pb-2 ${
            activeTab === tab.value
              ? "!text-[#1e2d3d] border-b-2 border-[#1e2d3d]"
              : "!text-gray-400 hover:!text-gray-600"
          }`}
        >
          {tab.label}
        </button>
      ))}
    </div>
  );
}

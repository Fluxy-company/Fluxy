import DashboardNavbar from "./DashboardNavbar";
import DashboardFooter from "./DashboardFooter";

export default function DashboardLayout({ children }) {
  return (
    <div className="min-h-screen flex flex-col bg-[#f5f5f7]">
      <DashboardNavbar />
      <main className="flex-1 w-full px-8 py-8">
        {children}
      </main>
      <DashboardFooter />
    </div>
  );
}

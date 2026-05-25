export default function Layout({ children }) {
  return (
    <div className="px-[clamp(1.5rem,5vw,3rem)]">
      <div className="mx-auto">
        {children}
      </div>
    </div>
  );
}